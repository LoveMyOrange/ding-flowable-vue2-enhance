/**
 * 该文件是为了按需加载，剔除掉了一些不需要的框架组件。
 * 减少了编译支持库包大小
 *
 * 当需要更多组件依赖时，在该文件加入即可
 */
import Vue from 'vue';
import {
  ConfigProvider,
  Layout,
  Avatar,
  Input,
  Rate,
  Slider,
  InputNumber,
  Button,
  Switch,
  Radio,
  Checkbox,
  Select,
  Card,
  Empty,
  Form,
  Row,
  Col,
  Modal,
  Table,
  Tabs,
  Icon,
  Steps,
  Alert,
  Tag,
  Popover,
  Drawer,
  Divider,
  DatePicker,
  TimePicker,
  message,
  Upload,
  Tooltip,
  FormModel,
  Collapse,
  Cascader,
  TreeSelect,
  Spin,
  Dropdown,
  Menu,
  Affix,
  Space,
} from 'ant-design-vue';

export default {
  input: Input,
  number: InputNumber,
  select: Select,
  checkbox: Checkbox,
  radio: Radio,
  rate: Rate,
  slider: Slider,
  switch: Switch,
  treeSelect: TreeSelect,
  cascader: Cascader,
};

Vue.use(ConfigProvider);
Vue.use(Tooltip);
Vue.use(Empty);
Vue.use(FormModel);
Vue.use(Collapse);
Vue.use(Layout);
Vue.use(Avatar);
Vue.use(Input);
Vue.use(Rate);
Vue.use(Slider);
Vue.use(InputNumber);
Vue.use(Button);
Vue.use(Switch);
Vue.use(Radio);
Vue.use(Checkbox);
Vue.use(Select);
Vue.use(Card);
Vue.use(Form);
Vue.use(Row);
Vue.use(Col);
Vue.use(Modal);
Vue.use(Table);
Vue.use(Tabs);
Vue.use(Icon);
Vue.use(Steps);
Vue.use(Alert);
Vue.use(Tag);
Vue.use(Popover);
Vue.use(Drawer), Vue.use(Divider);
Vue.use(DatePicker);
Vue.use(TimePicker);
Vue.use(Upload);
Vue.use(Spin);
Vue.use(Dropdown);
Vue.use(Menu);
Vue.use(Affix);
Vue.use(Cascader);
Vue.use(Space);

Vue.prototype.$confirm = Modal.confirm;
Vue.prototype.$message = message;
