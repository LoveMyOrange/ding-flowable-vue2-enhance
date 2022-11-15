<template>
  <div v-show="show" ref="screenshot" id="screenshot" class="screenshot">
    <div ref="screenshotDropdown" v-if="dropDownVisible" class="screenshot-dropdown">
      <div v-for="(menu, i) in menus" :key="i" @click="changeMenu(menu)">
        <img :src="settingBaseIcon" />
        <span>{{ menu.name }}</span>
      </div>
    </div>
    <a-button ref="screenshotBtn" id="screenshotBtn" @click.native.prevent.stop="showDropDown" class="screenshot-btn el-dropdown-link" type="danger" icon="menu-unfold"></a-button>
  </div>
</template>
<script>
  import { flowMixin } from '../../mixins/flowMixin';
  let body = document.querySelectorAll('body')[0];
  export default {
    name: 'MenuShot',
    mixins: [flowMixin],
    props: {
      menus: {
        type: Array,
        default: () => [],
      },
    },
    data() {
      return {
        show: true,

        startX: undefined, //判断是否要打开下拉
        endX: undefined,
        startY: undefined,
        endY: undefined,

        active: false, //判断当前是否在拖动状态
        currentX: undefined,
        currentY: undefined,
        initialX: undefined,
        initialY: undefined,

        xOffset: 0,
        yOffset: 0,
        dropDownVisible: false,
      };
    },
    mounted() {
      //注册拖拽事件
      this.$refs['screenshot'].addEventListener('touchstart', this.dragStart, false);
      this.$refs['screenshot'].addEventListener('mousedown', this.dragStart, false);
    },
    methods: {
      //隐藏下拉
      hideDropDown() {
        this.dropDownVisible = false;
        body.removeEventListener('click', this.hideDropDown, false);
        let screenshotBtn = this.$refs['screenshotBtn'].$el;
        if (screenshotBtn) {
          screenshotBtn.style.opacity = 0.2;
        }
      },
      //展示下拉
      showDropDown(e) {
        if (this.startX == this.endX && this.startY == this.endY) {
          this.dropDownVisible = !this.dropDownVisible;
          if (this.dropDownVisible == false) {
            this.hideDropDown();
          } else {
            body.addEventListener('click', this.hideDropDown, false);
            let screenshotBtn = this.$refs['screenshotBtn'].$el;
            if (screenshotBtn) {
              screenshotBtn.style.opacity = 1;
            }
          }
        }
      },
      setTranslate(xPos, yPos, el) {
        el.style.transform = 'translate3d(' + xPos + 'px, ' + yPos + 'px, 0)';
      },
      drag($event) {
        if (this.active) {
          if ($event.type === 'touchmove') {
            this.currentX = $event.touches[0].clientX - this.initialX;
            this.currentY = $event.touches[0].clientY - this.initialY;
          } else {
            this.currentX = $event.clientX - this.initialX;
            this.currentY = $event.clientY - this.initialY;
          }

          this.xOffset = this.currentX;
          this.yOffset = this.currentY;
          this.setTranslate(this.currentX, this.currentY, this.$refs['screenshot']);
        }
        return false;
      },
      dragEnd($event) {
        this.initialX = this.currentX;
        this.initialY = this.currentY;
        //判断是否手机touch事件
        if ($event.type === 'touchstart') {
          //记录结束的位置 当开始和结束的位置一样的时候，需要触发下拉菜单展示
          this.endX = $event.touches[0].clientX;
          this.endY = $event.touches[0].clientY;
        } else {
          //记录结束的位置 当开始和结束的位置一样的时候，需要触发下拉菜单展示
          this.endX = $event.clientX;
          this.endY = $event.clientY;
        }
        let screenshotBtn = this.$refs['screenshotBtn'].$el;
        if (screenshotBtn) {
          screenshotBtn.style.opacity = 0.2;
        }
        this.active = false;

        body.removeEventListener('touchend', this.dragEnd);
        body.removeEventListener('touchmove', this.drag);
        body.removeEventListener('mouseup', this.dragEnd);
        body.removeEventListener('mousemove', this.drag);
        return false;
      },
      dragStart($event) {
        if ($event.type === 'touchstart') {
          this.initialX = $event.touches[0].clientX - this.xOffset;
          this.initialY = $event.touches[0].clientY - this.yOffset;
          //记录开始的位置 当开始和结束的位置一样的时候，需要触发下拉菜单展示
          this.startX = $event.touches[0].clientX;
          this.startY = $event.touches[0].clientY;
        } else {
          this.initialX = $event.clientX - this.xOffset;
          this.initialY = $event.clientY - this.yOffset;
          //记录开始的位置 当开始和结束的位置一样的时候，需要触发下拉菜单展示
          this.startX = $event.clientX;
          this.startY = $event.clientY;
        }
        if ($event.currentTarget.id === this.$refs['screenshot'].id) {
          body.removeEventListener('touchend', this.dragEnd, false);
          body.removeEventListener('touchmove', this.drag, false);
          body.removeEventListener('mouseup', this.dragEnd, false);
          body.removeEventListener('mousemove', this.drag, false);
          body.addEventListener('touchend', this.dragEnd, false);
          body.addEventListener('touchmove', this.drag, false);
          body.addEventListener('mouseup', this.dragEnd, false);
          body.addEventListener('mousemove', this.drag, false);
          //是否是拖拽状态
          this.active = true;
        }
        let screenshotBtn = this.$refs['screenshotBtn'].$el;
        if (screenshotBtn) {
          screenshotBtn.style.opacity = 1;
        }

        return false;
      },
      changeMenu(menu) {
        this.$emit('change', menu);
      },
    },
  };
</script>
