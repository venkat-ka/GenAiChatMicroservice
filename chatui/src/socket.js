import { io } from "socket.io-client";
// const socket = io("http://localhost:9096/", {
//     withCredentials: true,
//     extraHeaders: {
//         "my-custom-header": "abcd"
//     }
// });

export const soket = io.connect('localhost:8082', {
    'path': '/kafka-prod/chat/ws-chat',
    withCredentials: true,
    extraHeaders: {
        "Access-Control-Allow-Origin": "*"
    }
});