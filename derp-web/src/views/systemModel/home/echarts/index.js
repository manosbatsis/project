// 品牌销售Top10柱状图
export const brandSaleTop10ColunmChart = (dom, { xAxisData, seriesData }) => {
  const option = {
    color: ['#3398DB'],
    xAxis: [
      {
        type: 'category',
        data: xAxisData,
        axisTick: {
          show: false
        },
        axisLabel: {
          interval: 0,
          rotate: 45,
          formatter: function (value) {
            const maxlength = 6
            if (value.length > maxlength) {
              return value.substring(0, maxlength - 1) + '...'
            } else {
              return value
            }
          }
        }
      }
    ],
    grid: {
      // 控制图的大小，调整下面这些值就可以，
      x: 70,
      x2: 100,
      y2: 60 // y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
    },
    tooltip: {
      trigger: 'item',
      formatter: '{b} <br/> 品牌销量 : {c}'
    },
    yAxis: [
      {
        type: 'value',
        axisTick: {
          show: false
        },
        name: '销量',
        nameLocation: 'end',
        splitLine: {
          // gird 区域中的分割线
          show: true, // 是否显示
          lineStyle: {
            color: '#F3F6FA',
            width: 2
          }
        }
      }
    ],
    label: { color: 'inherit' },
    series: [
      {
        type: 'bar',
        barWidth: '18',
        barGap: '60',
        barBorderRadius: [10, 10],
        data: seriesData,
        itemStyle: {
          normal: {
            barBorderRadius: [18, 18, 0, 0],
            colorStops: [
              {
                offset: 0,
                color: '#428AF5'
              },
              {
                offset: 1,
                color: '#60C5FF'
              }
            ]
          }
        }
      }
    ]
  }
  dom.setOption(option)
}

// 品牌销售Top10饼图
export const brandSaleTop10PieChart = (dom, data) => {
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '平台数据 <br/>{b} : {c} ({d}%)'
    },
    color: [
      '#946EC6',
      '#EF5665',
      '#F4A869',
      '#FFCCCC',
      '#FFCC99',
      '#0099FF',
      '#9966FF',
      '#33CC66',
      '#CC9933',
      '#FF0099'
    ],
    label: { color: 'inherit' },
    stillShowZeroSum: false,
    series: [
      {
        type: 'pie',
        radius: [0, '50%'],
        center: ['50%', '50%'],
        data,
        itemStyle: {
          emphasis: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(128, 128, 128, 0.5)'
          }
        }
      }
    ]
  }
  dom.setOption(option)
}

// 效期预警饼图
export const InventoryWarningPieChart = (dom, data) => {
  const legendData = [
    { value: '2/3以上', name: '2/3以上效期' },
    { value: '1/2<x≤2/3', name: '2/3效期' },
    { value: '1/3<x≤1/2', name: '1/2效期' },
    { value: '1/5<x≤1/3', name: '1/3效期' },
    { value: '1/10<x≤1/5', name: '1/5效期' },
    { value: '0<x≤1/10', name: '1/10效期' },
    { value: '过期品', name: '过期品' },
    { value: '残次品', name: '残次品' }
  ]
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    color: [
      '#FCD63D',
      '#DAA3FF',
      '#75B1FF',
      '#FF94C1',
      '#FFCC99',
      '#FF9900',
      '#6666CC',
      '#FF00CC'
    ],
    legend: {
      itemWidth: 14,
      itemHeight: 14,
      width: 300,
      itemGap: 26,
      orient: 'horizontal',
      bottom: 10,
      left: '10%',
      align: 'auto',
      selectedMode: false,
      formatter: function (name) {
        const target = legendData.find((item) => item.name === name)
        if (target) {
          return target.value
        }
      },
      icon: 'rectangle',
      textStyle: {
        rich: {
          a: {
            fontSize: 14,
            color: '#EA5504',
            padding: 12
          },
          b: {
            fontSize: 12,
            height: 12,
            lineHeight: 12,
            width: 60,
            color: '#333'
          }
        }
      }
    },
    label: { color: 'inherit' },
    series: [
      {
        name: '仓库数据',
        type: 'pie',
        radius: '50%',
        center: ['55%', '40%'],
        data
      }
    ]
  }
  dom.setOption(option)
}

// 创建事业部库存饼图
export const buStockPieChart = (dom, data) => {
  console.log(data)
  const option = {
    backgroundColor: '#fff',
    tooltip: {
      trigger: 'item',
      position: function (point, params, dom, rect, size) {
        // 鼠标坐标和提示框位置的参考坐标系是：以外层div的左上角那一点为原点，x轴向右，y轴向下
        // 提示框位置
        let x = 0 // x坐标位置
        let y = 0 // y坐标位置
        // 当前鼠标位置
        const pointX = point[0]
        const pointY = point[1]
        // 提示框大小
        const boxWidth = size.contentSize[0]
        const boxHeight = size.contentSize[1]
        // boxWidth > pointX 说明鼠标左边放不下提示框
        if (boxWidth > pointX) {
          x = 5
        } else {
          // 左边放的下
          x = pointX - boxWidth
        }
        // boxHeight > pointY 说明鼠标上边放不下提示框
        if (boxHeight > pointY) {
          y = 40
        } else {
          // 上边放得下
          /* y = pointY - boxHeight; */
          y = pointY - boxHeight - 20
        }
        return [x, y]
      },
      formatter: '事业部库存总量:{c} <br/> 占比比例: {d}%'
    },
    series: [
      {
        name: '仓库数据',
        type: 'pie',
        radius: '80%',
        center: ['53%', '50%'],
        label: {
          position: 'inside'
        },
        data
      }
    ]
  }
  dom.setOption(option)
}

// 创建事业部库存仪表盘
export const buStockPanelChart = (dom, chartData = []) => {
  const series = chartData.map(({ rate, color, data }, index) => {
    const pos = `${15 + 23 * index}%`
    return {
      type: 'gauge',
      radius: '50%',
      center: [pos, '55%'],
      splitNumber: 0, // 刻度数量
      startAngle: 225,
      endAngle: -45,
      axisLine: {
        show: true,
        lineStyle: {
          width: 15,
          color: [
            [rate, color],
            [1, '#ececec']
          ]
        }
      },
      // 分隔线样式。
      splitLine: {
        show: false
      },
      axisLabel: {
        show: false
      },
      axisTick: {
        show: false
      },
      pointer: {
        show: false
      },
      title: {
        show: true,
        offsetCenter: [0, '100%'], // x, y，单位px
        textStyle: {
          color: '#7C8B92',
          fontSize: 13
        }
      },
      // 仪表盘详情，用于显示数据。
      detail: {
        show: true,
        offsetCenter: [0, '0%'],
        color,
        size: 12,
        textStyle: {
          fontSize: 18
        }
      },
      data
    }
  })
  const option = {
    title: {
      text: '事业部仓库库存分布图',
      textStyle: {
        color: '#2D353E',
        fontSize: 18
      },
      left: 'center',
      top: '5%'
    },
    series
  }
  dom.setOption(option)
}
