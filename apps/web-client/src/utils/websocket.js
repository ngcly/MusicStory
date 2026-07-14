import SockJS from 'sockjs-client'
import Stomp from 'stompjs'

export const initWebSocket = () => {
  let stompClient = ''
  const socket = new SockJS(import.meta.env.VITE_BASE_API + '/chat')
  stompClient = Stomp.over(socket)
  stompClient.heartbeat.outgoing = 20000
  stompClient.heartbeat.incoming = 0
  return stompClient
}
