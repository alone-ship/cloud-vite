import { createApp } from 'vue'
import './style.css'
// @ts-ignore
import App from './App.vue'
import router from './plugins/router'
import { createPinia } from 'pinia'

import 'element-plus/theme-chalk/dark/css-vars.css'

const app = createApp(App)
const pinia = createPinia()
pinia.use(({ store }) => {
    const initialState = JSON.parse(JSON.stringify(store.$state))
    store.$reset = (): void => {
        store.$state = JSON.parse(JSON.stringify(initialState))
    }
})
//use
app.use(router)
app.use(pinia)

//global

app.mount('#app')
