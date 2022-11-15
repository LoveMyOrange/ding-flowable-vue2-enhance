import Vue from 'vue';
import Vuex from 'vuex';
import flow from '../../packages/store/index';

Vue.use(Vuex);

const store = new Vuex.Store({
  modules: {
    flow,
  },
});

export default store;
