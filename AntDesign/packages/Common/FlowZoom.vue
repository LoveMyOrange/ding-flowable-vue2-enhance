<template>
  <div class="flow-design-operate">
    <!-- <div class="flow-fullscreen">
      <span class="flow-fullscreen-icon"></span>
    </div>  -->
    <div class="flow-design-zoom">
      <div class="flow-design-zoom-less" @click="onZoomOut"></div>
      <span>{{ zoomValue }}%</span>
      <div class="flow-design-zoom-more" @click="onZoomIn"></div>
    </div>
  </div>
</template>
<script>
  const INIT_ZOOM_VALUE = 100;
  const MIN_ZOOM_VALUE = 50;
  const MAX_ZOOM_VALUE = 200;
  export default {
    name: 'FlowZoom',
    components: {},
    props: {
      min: {
        type: Number,
        required: false,
        default: MIN_ZOOM_VALUE,
      },
      max: {
        type: Number,
        required: false,
        default: MAX_ZOOM_VALUE,
      },
    },
    data() {
      return {
        zoomValue: INIT_ZOOM_VALUE,
      };
    },
    methods: {
      onZoomOut() {
        let value = this.zoomValue;
        value -= 10;
        if (value <= MIN_ZOOM_VALUE) {
          value = MIN_ZOOM_VALUE;
        }
        this.zoomValue = value;
        this.$emit('input', value);
      },
      onZoomIn() {
        let value = this.zoomValue;
        value += 10;
        if (value >= MAX_ZOOM_VALUE) {
          value = MAX_ZOOM_VALUE;
        }
        this.zoomValue = value;
        this.$emit('input', value);
      },
    },
  };
</script>
