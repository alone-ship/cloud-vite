import axios from 'axios'

axios.defaults.withCredentials = true

export const proxy = axios.create({
  headers: {
    Authorization: 'Basic YWxvbmUtc2hpcDoxMjM0NTY='
  }
})

export const authorize = axios.create({
  baseURL: 'http://localhost:8802'
})
