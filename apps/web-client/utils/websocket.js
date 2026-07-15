import SockJS from 'sockjs-client'
import Stomp from 'stompjs'

let stompClient = null

const websocket = {
  connection: function (store) {
    if (process.client) {
      let socket = new SockJS('http://localhost:8070/chat')
      stompClient = Stomp.over(socket)
      stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame)
      })
    }
  }
}

export default websocket
