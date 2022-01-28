import * as echarts from 'echarts'
import { getDepotUnsellableDetail } from '@a/dataInsight'
import { debounce } from '@u/tool'

// 创建柱形图
export const createColumnChart = (
  dom,
  { tooltipData = {}, xAxisData = [], seriesData = [], yAxisData = {} }
) => {
  const helper = seriesData.map((item) => ({
    type: 'bar',
    data: item.data || [],
    name: item.name || '',
    barWidth: 22,
    xAxisIndex: item.xAxisIndex || 0,
    z: item.z || 0,
    barGap: '70%',
    label: {
      show: item.showLabel || false, // 开启显示
      position: 'top', // 在上方显示
      textStyle: {
        // 数值样式
        fontSize: 9,
        color: item.labelColor || '#333'
      }
    },
    itemStyle: {
      color: {
        x: 0,
        y: 0,
        x2: 0,
        y2: 1,
        colorStops: [
          {
            offset: 0,
            color: item.color[0]
          },
          {
            offset: 1,
            color: item.color[1]
          }
        ]
      }
    },
    emphasis: {
      itemStyle: {
        color: {
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [
            {
              offset: 0,
              color: item.color[0]
            },
            {
              offset: 1,
              color: item.color[1]
            }
          ]
        }
      }
    }
  }))
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow',
        shadowStyle: {
          color: 'rgba(1, 18, 62, .03)'
        }
      },
      backgroundColor: 'rgba(1, 18, 62, .8)',
      textStyle: {
        color: '#fff'
      },
      borderWidth: 0
    },
    grid: {
      left: '1%',
      right: '0',
      bottom: '3%',
      top: '30%',
      containLabel: true
    },
    legend: {
      data: ['月度达成', '月度目标', '年度达成', '年度目标'],
      textStyle: {
        color: 'rgba(124, 139, 146, 1)',
        fontSize: 16
      },
      selectedMode: false,
      itemWidth: 40,
      itemHeight: 10,
      left: 0,
      icon: 'rect',
      orient: 'horizontal',
      itemGap: 56
    },
    xAxis: [
      {
        type: 'category',
        offset: 4,
        axisTick: {
          show: false
        },
        axisLine: {
          lineStyle: {
            color: '#F3F6FA'
          }
        },
        axisLabel: {
          color: '#757B80'
        },
        data: xAxisData
      },
      {
        type: 'category',
        data: xAxisData,
        axisLine: {
          show: false
        },
        axisTick: {
          show: false
        },
        axisLabel: {
          show: false
        },
        splitArea: {
          show: false
        },
        splitLine: {
          show: false
        }
      }
    ],
    yAxis: {
      name: yAxisData.name || '单位：万元',
      nameTextStyle: {
        color: 'rgba(117, 123, 128, 1)'
      },
      axisLine: {
        show: true,
        lineStyle: {
          color: '#F3F6FA'
        }
      },
      splitArea: { show: false },
      splitLine: {
        show: false
      },
      axisTick: {
        show: true
      },
      axisLabel: {
        color: '#757B80'
      }
    },
    series: helper
  }
  if (tooltipData && tooltipData.type) {
    option.tooltip.formatter = formatterMap[tooltipData.type].bind(
      null,
      seriesData
    )
  }
  dom.setOption(option)
}

// 创建横向柱形图
export const createColumnChartX = (dom, { seriesData = [] }) => {
  const option = {
    legend: {
      data: ['月度达成', '月度目标', '年度达成', '年度目标'],
      textStyle: {
        color: 'rgba(124, 139, 146, 1)'
      },
      icon: 'rect',
      selectedMode: false,
      itemWidth: 40,
      itemHeight: 10,
      left: 0,
      orient: 'horizontal',
      itemGap: 56
    },
    grid: {
      left: '0',
      right: '15%',
      bottom: '3%',
      top: '14%',
      containLabel: true
    },
    xAxis: {
      type: 'value',
      offset: 4,
      name: '单位/万',
      nameTextStyle: {
        color: 'rgba(117, 123, 128, 1)'
      },
      axisTick: {
        show: false
      },
      axisLine: {
        show: true,
        lineStyle: {
          color: 'rgba(243, 246, 250, 1)'
        }
      },
      axisLabel: {
        color: 'rgba(117, 123, 128, 1)',
        formatter: '{value}',
        interval: 0
      },
      splitLine: {
        show: false
      }
    },
    yAxis: [
      {
        type: 'category',
        axisTick: {
          show: false
        },
        axisLine: {
          lineStyle: {
            color: '#F3F6FA'
          }
        },
        axisLabel: {
          color: '#757B80'
        },
        data: ['']
      },
      {
        type: 'category',
        data: [''],
        axisLine: {
          show: false
        },
        axisTick: {
          show: false
        },
        axisLabel: {
          show: false
        },
        splitArea: {
          show: false
        },
        splitLine: {
          show: false
        }
      }
    ],
    series: [
      {
        name: seriesData[0].name,
        type: 'bar',
        data: seriesData[0].data,
        barWidth: 22,
        // label: {
        //   show: seriesData[0].showLabel || false, // 开启显示
        //   position: 'top', // 在上方显示
        //   textStyle: { // 数值样式
        //     fontSize: 9,
        //     color: seriesData[0].labelColor || '#333'
        //   }
        // },
        itemStyle: {
          show: true,
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            {
              offset: 0,
              color: '#399BFF'
            },
            {
              offset: 1,
              color: '#0069E0'
            }
          ])
        },
        z: 3,
        yAxisIndex: 1,
        barGap: '70%',
        label: {
          show: true,
          position: 'insideRight',
          formatter: '{c}万',
          color: '#fff'
        },
        emphasis: {
          itemStyle: {
            color: new echarts.graphic.LinearGradient(1, 0, 0, 0, [
              {
                offset: 0,
                color: 'rgba(57, 155, 255, 1)'
              },
              {
                offset: 1,
                color: 'rgba(0, 105, 224, 1)'
              }
            ])
          }
        }
      },
      {
        name: seriesData[1].name,
        type: 'bar',
        data: seriesData[1].data,
        barWidth: 22,
        itemStyle: {
          show: true,
          barBorderRadius: [0, 6, 6, 0],
          color: 'rgba(224, 241, 251, 1)'
        },
        // label: {
        //   show: seriesData[1].showLabel || false, // 开启显示
        //   position: 'top', // 在上方显示
        //   textStyle: { // 数值样式
        //     fontSize: 9,
        //     color: seriesData[1].labelColor || '#333'
        //   }
        // },
        barGap: '70%',
        label: {
          show: true,
          color: '#757B80',
          position: 'right',
          formatter: '{c}万'
        },
        emphasis: {
          itemStyle: {
            color: 'rgba(224, 241, 251, 1)'
          }
        }
      },
      {
        name: seriesData[2].name,
        type: 'bar',
        data: seriesData[2].data,
        barWidth: 22,
        itemStyle: {
          show: true,
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            {
              offset: 0,
              color: '#FFB10B'
            },
            {
              offset: 1,
              color: '#FF720B'
            }
          ])
        },
        // label: {
        //   show: seriesData[2].showLabel || false, // 开启显示
        //   position: 'top', // 在上方显示
        //   textStyle: { // 数值样式
        //     fontSize: 9,
        //     color: seriesData[2].labelColor || '#333'
        //   }
        // },
        yAxisIndex: 1,
        z: 3,
        barGap: '70%',
        label: {
          show: true,
          color: '#fff',
          position: 'insideRight',
          formatter: '{c}万'
        },
        emphasis: {
          itemStyle: {
            color: new echarts.graphic.LinearGradient(1, 0, 0, 0, [
              {
                offset: 0,
                color: '#FFB10B'
              },
              {
                offset: 1,
                color: '#FF720B'
              }
            ])
          }
        }
      },
      {
        name: seriesData[3].name,
        type: 'bar',
        data: seriesData[3].data,
        barWidth: 22,
        itemStyle: {
          show: true,
          barBorderRadius: [0, 6, 6, 0],
          color: 'rgba(240, 240, 240, 1)'
        },
        // label: {
        //   show: seriesData[3].showLabel || false, // 开启显示
        //   position: 'top', // 在上方显示
        //   textStyle: { // 数值样式
        //     fontSize: 9,
        //     color: seriesData[3].labelColor || '#333'
        //   }
        // },
        barGap: '70%',
        label: {
          show: true,
          color: '#757B80',
          position: 'right',
          formatter: '{c}万'
        },
        emphasis: {
          itemStyle: {
            color: 'rgba(240, 240, 240, 1)'
          }
        }
      }
    ]
  }
  dom.setOption(option)
}

// 创建折线、柱图
export const createPolylineColumn = (
  dom,
  { yAxis, legendData = [], xAxisData = [], tooltipData = {}, seriesData = [] }
) => {
  const helper = seriesData.map((item) => {
    return item.type === 'bar'
      ? {
          name: item.name,
          yAxisIndex: item.yAxisIndex || 0,
          type: 'bar',
          barWidth: 22,
          barGap: item.barGap || '70%',
          label: {
            show: item.showLabel || false, // 开启显示
            position: 'top', // 在上方显示
            textStyle: {
              // 数值样式
              fontSize: 10,
              color: item.labelColor || '#333'
            }
          },
          itemStyle: {
            color: {
              type: 'linear',
              x: 0,
              y: 0,
              x2: 0,
              y2: 1,
              colorStops: [
                {
                  offset: 0,
                  color: item.color[0] // 0% 处的颜色
                },
                {
                  offset: 1,
                  color: item.color[1] // 100% 处的颜色
                }
              ]
            }
          },
          data: item.data
        }
      : {
          name: item.name,
          yAxisIndex: item.yAxisIndex || 0,
          type: 'line',
          itemStyle: {
            show: true,
            color: item.color
          },
          lineStyle: {
            width: 4,
            shadowColor: 'rgba(0,0,0,0.1)',
            shadowBlur: 6,
            shadowOffsetY: 7
          },
          symbol: 'circle',
          symbolSize: 6,
          data: item.data
        }
  })
  const option = {
    grid: {
      left: '3%',
      right: '10%',
      bottom: '3%',
      top: '25%',
      containLabel: true
    },
    legend: {
      data: legendData,
      textStyle: {
        color: 'rgba(124, 139, 146, 1)',
        fontSize: 16
      },
      icon: 'rect',
      selectedMode: false,
      itemWidth: 40,
      itemHeight: 10,
      left: 0,
      top: 10,
      orient: 'horizontal',
      itemGap: 56
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow',
        shadowStyle: {
          color: 'rgba(1, 18, 62, .1)'
        }
      },
      backgroundColor: 'rgba(1, 18, 62, .8)',
      textStyle: {
        color: '#fff',
        fontSize: 16
      },
      borderWidth: 0
    },
    xAxis: [
      {
        type: 'category',
        offset: 4,
        axisTick: {
          alignWithLabel: true
        },
        axisLine: {
          show: true,
          lineStyle: {
            color: '#757B80',
            width: 1
          }
        },
        data: xAxisData
      }
    ],
    yAxis: yAxis || [
      {
        type: 'value',
        name: '单位：万元',
        position: 'left',
        axisTick: {
          show: false
        },
        nameTextStyle: {
          color: 'rgba(117, 123, 128, 1)'
        },
        axisLine: {
          show: true,
          lineStyle: {
            color: '#F3F6FA',
            width: 2
          }
        },
        axisLabel: {
          color: '#757B80'
        },
        splitLine: {
          show: true,
          lineStyle: {
            color: '#F3F6FA',
            type: 'dotted'
          }
        }
      }
    ],
    series: helper
  }
  if (tooltipData && tooltipData.type) {
    option.tooltip.formatter = formatterMap[tooltipData.type].bind(
      null,
      seriesData
    )
  }
  dom.setOption(option)
}

// 创建饼状图
export const createPieChart = (dom, { title = '', seriesData = [] }) => {
  const helper = seriesData.map((item) => ({
    value: item.value,
    name: item.name,
    itemStyle: {
      color: {
        type: 'linear',
        x: 0,
        y: 0,
        x2: 0,
        y2: 1,
        colorStops: [
          {
            offset: 0,
            color: item.color[0]
          },
          {
            offset: 1,
            color: item.color[1]
          }
        ]
      }
    },
    label: {
      rich: {
        hr: {
          borderRadius: 3,
          width: 3,
          height: 3,
          borderWidth: 1,
          borderColor: item.borderColor,
          padding: [3, 3, 0, -12]
        },
        a: {
          padding: [-13, 15, -20, 5]
        }
      }
    }
  }))
  const option = {
    title: {
      text: title,
      textStyle: {
        color: '#2D353E',
        fontSize: 18
      },
      left: 'center',
      top: '5%'
    },
    tooltip: {
      trigger: 'item',
      formatter: '{b} : {c} ({d}%)',
      axisPointer: {
        type: 'shadow',
        shadowStyle: {
          color: 'rgba(1, 18, 62, .1)'
        }
      },
      backgroundColor: 'rgba(1, 18, 62, .8)',
      textStyle: {
        color: '#fff'
      },
      borderWidth: 0
    },
    series: [
      {
        type: 'pie',
        center: ['50%', '54%'],
        radius: 120,
        label: {
          color: '#7C8B92',
          position: 'outside',
          formatter: '{a|{b}：{d}%}\n{hr|}'
        },
        labelLine: {
          smooth: true,
          length2: 8
        },
        data: helper
      }
    ]
  }
  dom.setOption(option)
}

// 创建玫瑰图
export const createRoseChart = (dom, { title = '', seriesData = [] }) => {
  const helper = seriesData.map((item) => ({
    value: item.value,
    name: item.name,
    itemStyle: {
      color: {
        type: 'linear',
        x: 0,
        y: 0,
        x2: 0,
        y2: 1,
        colorStops: [
          {
            offset: 0,
            color: item.color[0]
          },
          {
            offset: 1,
            color: item.color[1]
          }
        ]
      }
    },
    label: {
      rich: {
        hr: {
          borderRadius: 3,
          width: 3,
          height: 3,
          borderWidth: 1,
          borderColor: item.borderColor,
          padding: [3, 3, 0, -12]
        },
        a: {
          padding: [-13, 15, -20, 5]
        }
      }
    }
  }))
  const option = {
    title: {
      text: title,
      textStyle: {
        color: '#2D353E',
        fontSize: 18
      },
      left: 'center'
    },
    tooltip: {
      trigger: 'item',
      formatter: '{b} : {c} ({d}%)',
      axisPointer: {
        type: 'shadow',
        shadowStyle: {
          color: 'rgba(1, 18, 62, .1)'
        }
      },
      backgroundColor: 'rgba(1, 18, 62, .8)',
      textStyle: {
        color: '#fff'
      },
      borderWidth: 0
    },
    series: [
      {
        type: 'pie',
        center: ['50%', '60%'],
        radius: ['38%', '70%'],
        roseType: 'radius',
        label: {
          color: '#7C8B92',
          formatter: '{a|{b}：{d}%}\n{hr|}'
        },
        labelLine: {
          smooth: true,
          length2: 8
        },
        itemStyle: {
          borderWidth: 1,
          borderColor: '#fff'
        },
        data: helper
      }
    ]
  }
  dom.setOption(option)
}

// 创建堆叠柱形图
export const createStackColumnChart = (
  dom,
  {
    yAxisData = [],
    tooltipData = {},
    legendData = [],
    seriesData = [],
    fetchData = {}
  }
) => {
  const helper = seriesData.map((item) => ({
    name: item.name,
    type: 'bar',
    stack: 'total',
    barWidth: 25,
    label: {
      show: true,
      color: '#ffffff'
    },
    itemStyle: {
      color: item.color
    },
    emphasis: {
      focus: 'series'
    },
    data: item.data
  }))
  const option = {
    title: {
      show: true,
      text: '库存金额 单位：万元',
      textStyle: {
        fontSize: 12,
        color: '#757B80'
      },
      bottom: '12',
      right: '26'
    },
    tooltip: {
      show: true,
      axisPointer: {
        type: 'shadow'
      },
      shadowStyle: {
        color: 'rgb(128, 128, 128)'
      },
      backgroundColor: 'rgba(1, 18, 62, .8)',
      textStyle: {
        color: '#fff'
      },
      borderWidth: 0
      // formatter: formatterMap['stack'].bind(null, seriesData, fetchData)
    },
    legend: {
      data: legendData,
      textStyle: {
        fontSize: 16,
        color: '#757B80'
      },
      icon: 'rect',
      selectedMode: false,
      itemWidth: 40,
      itemHeight: 10,
      left: 20,
      orient: 'horizontal',
      itemGap: 25
    },
    grid: {
      left: '3%',
      top: '20%',
      containLabel: true
    },
    xAxis: {
      type: 'value',
      offset: 4,
      nameGap: 16,
      axisLine: {
        show: true,
        lineStyle: {
          color: '#F3F6FA'
        }
      },
      axisLabel: {
        color: '#757B80'
      },
      axisTick: {
        show: true
      },
      splitLine: {
        show: false
      },
      // max: 1000,
      splitNumber: 10
    },
    yAxis: {
      type: 'category',
      axisLine: {
        show: true,
        lineStyle: {
          color: '#F3F6FA'
        }
      },
      axisLabel: {
        color: '#757B80'
      },
      axisTick: {
        show: false
      },
      data: yAxisData
    },
    series: helper
  }
  if (tooltipData && tooltipData.type) {
    if (tooltipData.type === 'delay') {
      option.tooltip.formatter = formatterMap[tooltipData.type].bind(
        null,
        seriesData,
        fetchData
      )
    } else {
      option.tooltip.formatter = formatterMap[tooltipData.type].bind(
        null,
        seriesData
      )
    }
  }
  dom.setOption(option)
}

const formatterMap = {
  delay: debounce((seriesData, fetchData, param, origin, callback) => {
    const { seriesName, name } = param
    const handleFetch = async () => {
      const target = seriesData.find((item) => item.name === seriesName) || {}
      const { id } = target
      // 在仓天数区间划分为：1: 0~30天;2: 30天~60天;3: 60天~90天; 4: 90天~120天;5: 120天以上
      const helper = {
        '0~30天': 1,
        '30天~60天': 2,
        '60天~90天': 3,
        '90天~120天': 4,
        '120天以上': 5
      }
      try {
        const { data } = await getDepotUnsellableDetail({
          ...fetchData,
          inWarehouseInterval: helper[name],
          brandParentId: id
        })
        const maxNum =
          (target.data &&
            target.data.reduce((prev, cur) => {
              return prev + (cur ? +cur : 0)
            }, 0)) ||
          0
        // 拼接formatter结构
        let str = '<div style="backgroundColor: #01123E;padding: 0px 10px;">'
        str +=
          '<div style="color: #fff; margin-top: 0px;"><span style="display:inline-block;margin-right:4px;border-radius:10px;width:10px;height:10px;background-color:' +
          param.color +
          '"></span><span style="font-size:14px;">' +
          param.seriesName +
          ' (' +
          param.name +
          ')</span></div>'
        str +=
          '<div style="color: #fff; margin-top: 0px;"><span style="display:inline-block;margin-right:4px;border-radius:10px;width:10px;height:10px;opacity: 0;"></span><span style="font-size:12px;">' +
          '总库存金额: ' +
          (+maxNum).toFixed(2) +
          '万元</span></div>'
        str +=
          '<div style="color: #fff; margin: 10px 0;border-top: dotted 1px #fff;"></div>'
        data &&
          data.length &&
          data.forEach(({ brandName, totalAmount }) => {
            str +=
              '<div style="color: #fff; margin-top: 0px;"><span style="display:inline-block;margin-right:4px;border-radius:10px;width:10px;height:10px;opacity: 0;"></span><span style="font-size:12px;">' +
              (brandName || '') +
              ': ' +
              (totalAmount || 0) +
              '万元</span></div>'
          })
        const html = str + '</div>'
        callback(origin, html)
        // return html
      } catch (error) {
        console.log(error.message)
      }
    }
    handleFetch()
    return 'loading...'
  }, 300),
  expire(seriesData, param) {
    const { dataIndex } = param
    let maxNum = 0
    if (seriesData && seriesData.length) {
      const arr = []
      seriesData.forEach((item) => {
        arr.push(...item.data)
      })
      maxNum =
        arr.reduce(function (prev, cur) {
          return prev + (cur ? +cur : 0)
        }, 0) || 0
    }
    let str = '<div style="backgroundColor: #01123E;padding: 0px 10px;">'
    str +=
      '<div style="color: #fff; margin-top: 0px;"><span style="display:inline-block;margin-right:4px;border-radius:10px;width:10px;height:10px;background-color:' +
      param.color +
      '"></span><span style="font-size:14px;">' +
      param.seriesName +
      ' (' +
      param.name +
      ')</span></div>'
    str +=
      '<div style="color: #fff; margin-top: 0px;"><span style="display:inline-block;margin-right:4px;border-radius:10px;width:10px;height:10px;opacity: 0;"></span><span style="font-size:12px;">' +
      '总库存金额: ' +
      (+maxNum).toFixed(2) +
      '万元</span></div>'
    str +=
      '<div style="color: #fff; margin: 10px 0;border-top: dotted 1px #fff;"></div>'
    for (let i = 0; i < seriesData.length; i++) {
      const seriesName = seriesData[i].name
      const value = seriesData[i].data[dataIndex]
      str +=
        '<div style="color: #fff; margin-top: 0px;"><span style="display:inline-block;margin-right:4px;border-radius:10px;width:10px;height:10px;opacity: 0;"></span><span style="font-size:12px;">' +
        seriesName +
        ': ' +
        value +
        '万元</span></div>'
    }
    const html = str + '</div>'
    return html
  },
  achieve(seriesData, param) {
    // 数组排序
    function createComprisonFunction(propertyName) {
      return function (object1, object2) {
        var value1 = object1[propertyName]
        var value2 = object2[propertyName]
        if (value1 < value2) {
          return -1
        } else if (value1 > value2) {
          return 1
        } else {
          return 0
        }
      }
    }
    // 小数转百分数
    function toPercent(point) {
      var str = (Number(point * 100) || 0).toFixed(2)
      str += '%'
      return str
    }
    const arr2 = param.sort(createComprisonFunction('seriesName')).reverse()
    var str = ''
    // eslint-disable-next-line
    let val1 =
      arr2[1].value && arr2[1].value > 0
        ? toPercent(arr2[0].value / arr2[1].value)
        : '-'
    // eslint-disable-next-line
    let val2 =
      arr2[3].value && arr2[3].value > 0
        ? toPercent(arr2[2].value / arr2[3].value)
        : '-'
    arr2.forEach((item, index) => {
      str +=
        '<div>' + item.marker + item.seriesName + ':' + item.value + '<div>'
      if (index === 1) {
        str +=
          '<div style="color:#AFD6FF;padding:5px 0;">达成率：' + val1 + '</div>'
        str +=
          '<div style="background:#fff;width:100%;height:1px;margin:6px 0 8px 0;"></div>'
      }
      if (index === 3) {
        str +=
          '<div style="color:#AFD6FF;padding:5px 0 0 0;">达成率：' +
          val2 +
          '</div>'
      }
    })
    return str
  },
  brand(seriesData, param) {
    const val1 = param[0] ? param[0].value : 0
    const axisValueLabel = param[0] ? param[0].axisValueLabel : 0
    const html =
      '<div style="backgroundColor: #01123E;padding: 0px 10px;">' +
      '<div style="color: #fff; font-size:14px;"><span style="display:inline-block;margin-right:4px;border-radius:10px;width:10px;height:10px;background-color:rgba(0, 105, 224, 1);"></span><span>' +
      axisValueLabel +
      '</span></div>' +
      '<div style="color: #fff; margin-top: 0px;"><span style="display:inline-block;margin-right:4px;border-radius:10px;width:10px;height:10px;opacity: 0;opacity: 0;"></span><span style="font-size:12px;">销量：' +
      val1 +
      '万</span></div>' +
      '</div>'
    return html
  },
  annual(seriesData, param) {
    const val1 = param[0] ? param[0].value : 0
    const val2 = param[1] ? param[1].value : 0
    const val3 = param[2] ? param[2].value : 0
    const html =
      '<div style="backgroundColor: #01123E;padding: 0px 10px;">' +
      '<div style="color: #fff; font-size:14px;"><span style="display:inline-block;margin-right:4px;border-radius:10px;width:10px;height:10px;background-color:rgba(0, 105, 224, 1);"></span><span>采购</span></div>' +
      '<div style="color: #fff; margin-top: -5px;"><span style="display:inline-block;margin-right:4px;border-radius:10px;width:10px;height:10px;opacity: 0;opacity: 0;"></span><span style="font-size:12px;">金额：' +
      val1 +
      '万</span></div>' +
      '<div style="color: #fff; font-size:14px;"><span style="display:inline-block;margin-right:4px;border-radius:10px;width:10px;height:10px;background-color:rgba(255, 144, 11, 1);"></span><span>销售</span></div>' +
      '<div style="color: #fff; margin-top: -5px;"><span style="display:inline-block;margin-right:4px;border-radius:10px;width:10px;height:10px;opacity: 0;opacity: 0;"></span><span style="font-size:12px;">销量：' +
      val2 +
      '万</span></div>' +
      '<div style="color: #fff; font-size:14px;"><span style="display:inline-block;margin-right:4px;border-radius:10px;width:10px;height:10px;background-color:rgba(48, 211, 216, 1);"></span><span>库存</span></div>' +
      '<div style="color: #fff; margin-top: -5px;"><span style="display:inline-block;margin-right:4px;border-radius:10px;width:10px;height:10px;opacity: 0;opacity: 0;"></span><span style="font-size:12px;">销量：' +
      val3 +
      '万</span></div>' +
      '</div>'
    return html
  },
  stock(seriesData, param) {
    const val1 = param[0] ? param[0].value : 0
    const val2 = param[1] ? param[1].value : 0
    const val3 = param[2] ? param[2].value : 0
    const html =
      '<div style="backgroundColor: #01123E;padding: 0px 10px;">' +
      '<div style="color: #fff; font-size:14px;"><span style="display:inline-block;margin-right:4px;border-radius:10px;width:10px;height:10px;background-color:rgba(0, 105, 224, 1);"></span><span>库存量</span></div>' +
      '<div style="color: #fff; margin-top: -2px;"><span style="display:inline-block;margin-right:4px;border-radius:10px;width:10px;height:10px;opacity: 0;opacity: 0;"></span><span style="font-size:12px;">' +
      val1 +
      '万件</span></div>' +
      '<div style="color: #fff; font-size:14px;"><span style="display:inline-block;margin-right:4px;border-radius:10px;width:10px;height:10px;background-color:rgba(255, 144, 11, 1);"></span><span>库存金额</span></div>' +
      '<div style="color: #fff; margin-top: -2px;"><span style="display:inline-block;margin-right:4px;border-radius:10px;width:10px;height:10px;opacity: 0;opacity: 0;"></span><span style="font-size:12px;">' +
      val2 +
      '万</span></div>' +
      '<div style="color: #fff; font-size:14px;"><span style="display:inline-block;margin-right:4px;border-radius:10px;width:10px;height:10px;background-color:rgba(48, 211, 216, 1);"></span><span>预计清空天数</span></div>' +
      '<div style="color: #fff; margin-top: -2px;"><span style="display:inline-block;margin-right:4px;border-radius:10px;width:10px;height:10px;opacity: 0;opacity: 0;"></span><span style="font-size:12px;">' +
      val3 +
      '天</span></div>' +
      '</div>'
    return html
  },
  wares(seriesData, param) {
    const val1 = param[0] ? param[0].value : 0
    const val2 = param[1] ? param[1].value : 0
    // const val3 = param[2] ? param[2].value : 0
    const html =
      '<div style="backgroundColor: #01123E;padding: 0px 10px;">' +
      '<div style="color: #fff; font-size:14px;"><span style="display:inline-block;margin-right:4px;border-radius:10px;width:10px;height:10px;background-color:rgba(0, 105, 224, 1);"></span><span>库存量</span></div>' +
      '<div style="color: #fff; margin-top: -5px;"><span style="display:inline-block;margin-right:4px;border-radius:10px;width:10px;height:10px;opacity: 0;opacity: 0;"></span><span style="font-size:12px;">' +
      val1 +
      '件</span></div>' +
      '<div style="color: #fff; font-size:14px;"><span style="display:inline-block;margin-right:4px;border-radius:10px;width:10px;height:10px;background-color:rgba(255, 144, 11, 1);"></span><span>库存金额</span></div>' +
      '<div style="color: #fff; margin-top: -5px;"><span style="display:inline-block;margin-right:4px;border-radius:10px;width:10px;height:10px;opacity: 0;opacity: 0;"></span><span style="font-size:12px;">' +
      val2 +
      '万</span></div>' +
      '</div>'
    return html
  },
  waresBrand(seriesData, param) {
    const val1 = param[0] ? param[0].value : 0
    const axisValueLabel = param[0] ? param[0].axisValueLabel : 0
    const html =
      '<div style="backgroundColor: #01123E;padding: 0px 10px;">' +
      '<div style="color: #fff; font-size:14px;"><span style="display:inline-block;margin-right:4px;border-radius:10px;width:10px;height:10px;background-color:rgba(0, 105, 224, 1);"></span><span>' +
      axisValueLabel +
      '</span></div>' +
      '<div style="color: #fff; margin-top: 0px;"><span style="display:inline-block;margin-right:4px;border-radius:10px;width:10px;height:10px;opacity: 0;opacity: 0;"></span><span style="font-size:12px;">库存量：' +
      val1 +
      '件</span></div>' +
      '</div>'
    return html
  }
}
