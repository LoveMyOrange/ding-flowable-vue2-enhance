import { createApp } from 'vue'
import App from './App.vue'
// import { store } from './store'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

import "@/assets/theme.less";
import "@/assets/global.css";
// import Ellipsis from '@/views/common/Ellipsis'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app.use(ElementPlus);
// app.use(Ellipsis);
// store(app)

app.mount('#app')
