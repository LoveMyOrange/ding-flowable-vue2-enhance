<template>
  <div id="flow-design-map" class="flow-design-map">
    <img :src="$store.state.flow.mapImg" />
    <div id="flow-design-map-mask" class="map-mask" :style="mapMask" @mousedown="handleMouseDown" @mouseup="handleMouseUp" @mouseleave="handleMouseUp"></div>
  </div>
</template>
<script>
  import { flowMixin } from '../mixins/flowMixin';
  export default {
    name: 'FlowMap',
    components: {},
    mixins: [flowMixin],
    props: {
      element: {
        type: String,
        default: '#flow-design-content',
      },
    },
    data() {
      return {
        // 流程设计窗口
        flowDesign: null,
        // 地图窗口
        flowMap: null,
        // 地图窗口占比窗口(红色窗口)
        flowMapMsk: null,
        // 流程设计整体高度,包含滚动条隐藏高度
        wrapHeight: 0,
        // 地图红色窗口top位置
        top: 0,
        // 鼠标是否点击
        mouseDown: false,
      };
    },
    computed: {
      // 流程图视窗高度与流程总高度的比
      scaleHeight() {
        return this.containerHeight / this.wrapHeight;
      },
      // 小地图高度与流程总高度的比
      scaleOffsetHeight() {
        if (!this.flowMap && this.wrapHeight) {
          return 1;
        }
        return this.flowMap.clientHeight / this.wrapHeight;
      },
      // 流程图视窗高度
      containerHeight() {
        if (this.flowDesign) {
          return this.flowDesign.clientHeight;
        }
        return 0;
      },
      // 地图窗口占比窗口(红色窗口)样式
      mapMask() {
        return {
          width: '100%',
          height: `${this.scaleHeight * 100}%`,
          left: '0px',
          top: `${this.top}px`,
        };
      },
    },
    mounted() {
      this.$nextTick(() => {
        this.flowDesign = document.querySelector('#flow-design');
        this.flowMap = document.querySelector('#flow-design-map');
        this.flowMapMsk = document.querySelector('#flow-design-map-mask');
        this.initSize(this.flowDesign);
        this.$store.dispatch('flow/updateMap', { element: this.element });

        // 监听滚动条
        window.addEventListener('scroll', this.handleScroll, true);
        // 监听出现滚动条
        const timer = setInterval(() => {
          if (this.flowDesign.scrollHeight > this.flowDesign.clientHeight) {
            this.scroll = true;
          } else {
            this.scroll = false;
          }
          this.initSize(this.flowDesign);
        }, 1000);
        this.$once('hook:beforeDestroy', () => {
          clearInterval(timer);
        });

        // 监听鼠标移动
        window.addEventListener('mousemove', this.handleMouseMove);
        // 监听页面的mouseleave事件，当鼠标移出浏览器页面可用区域 并 松开按键时，停止拖动
        window.addEventListener('mouseleave', this.handleMouseUp);
      });
    },
    methods: {
      initSize(flowDesign) {
        this.wrapHeight = flowDesign.scrollHeight;
      },
      handleScroll(e) {
        this.top = this.flowDesign.scrollTop * this.scaleOffsetHeight;
      },
      handleMouseDown(e) {
        const me = this;
        this.mouseDown = true;
      },
      handleMouseMove(e) {
        if (this.mouseDown) {
          const directionY = e.movementY || e.mozMovementY || e.webkitMovementY || 0;
          if (directionY >= 0 && this.flowMapMsk.offsetHeight + this.top < this.flowMap.offsetHeight) {
            this.top++;
          } else if (this.top > 0) {
            this.top--;
          }
          this.flowDesign.scrollTop = this.top / this.scaleOffsetHeight;
        }
      },
      handleMouseUp(e) {
        this.mouseDown = false;
        document.onmousemove = document.onmouseup = null;
      },
    },
  };
</script>
