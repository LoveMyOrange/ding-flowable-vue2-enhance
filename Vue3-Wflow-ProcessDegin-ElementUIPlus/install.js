import ProcessDesign from '@/views/process/ProcessDesign.vue'

const components = [
    ProcessDesign
]

ProcessDesign.install = function (app) {
    app.component(ProcessDesign.name, ProcessDesign)
}

const install = (app) => {
    components.forEach(component => {
        app.component(component.name, component)
    })
}

//默认导出组件
export default {
    install,
    ProcessDesign,
};

