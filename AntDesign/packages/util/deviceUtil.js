const w = document.documentElement.clientWidth;
const h = document.documentElement.clientHeight;

const scale = {
  //屏幕缩放
  sizeScale: () => {
    //固定宽高及比
    let fixed_wh = { w: 1280, h: 609, r: 1280 / 609 };
    let ratio = w / fixed_wh.w;
    return ratio;
  },
  //是否是移动端
  isMobile: () => {
    let flag = navigator.userAgent.match(
      /(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i,
    );
    return flag;
  },
  //是否是横屏
  isOrientation: () => {
    let screen = false;
    if (w < h) {
      //竖屏
      screen = true;
    }
    return screen;
  },
  isScale: () => {
    let ismobile = scale.isMobile();
    let rscale = scale.sizeScale();
    let isclass = false;
    if (ismobile === null) {
      isclass = false;
      return { r: 1, c: isclass };
    } else {
      isclass = true;
      return { r: rscale, c: isclass };
    }
  },
};
export default scale;
