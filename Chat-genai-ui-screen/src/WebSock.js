
import { useState, useContext, useEffect } from 'react';
import InputText from './Component/UserElement/InputText.jsx'
import WebSockMessage from './WebSockMessage.jsx'
import SockJsClient from "react-stomp"
import WebChatApi from './service/WebChatApi.ts'
import AuthContext from './ContextData/AuthContext.ts';
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
//import LoginComponent from '.LoginComponent.js';
const SOCKET_URL = 'http://localhost:8082/kafka-con/ws-chat';
//const websocketUrl = 'ws://host.docker.internal:9092/websocket'
//const websocketUrl = 'http://localhost:8082/kafka-prod/chat/ws-chat';
//const SOCKET_URL = 'http://localhost:9092'
//const SOCKET_URL = 'http://localhost:9094';
//const websocketUrl = 'http://localhost:9096';

const websocketUrl = 'ws://localhost:8082/kafka-prod/ws-chat';

// const WebSock = () => {

//     let client: Client | null = null;

//     const [chatId, setChatId] = useState("")



//     useEffect(() => {
//         client = new Client();
//         // // WebSocket connection
//         // const socket = new SockJS(SOCKET_URL);
//         // const stompClient = new Client({
//         //     webSocketFactory: () => socket,
//         //     debug: (str) => console.log(str),
//         //     onConnect: () => {

//         //         console.log("Connected to WebSocket");

//         //         stompClient.subscribe("/topic/data-update", () => {
//         //             console.log("Triggered");
//         //         });
//         //     },
//         //     onStompError: () => {
//         //         console.log("error");
//         //     },
//         // });

//         // stompClient.activate();

//         // return () => {
//         //     stompClient.deactivate();
//         // };
//         //https://lejdiprifti.com/2023/10/15/configuring-websocket-in-spring-boot-and-react/
//         client.configure({
//             brokerURL: websocketUrl,

//             debug: function (str) {
//                 console.log(str);
//             },
//             //    connectHeaders: {
//             //     Accept: 'application/json',
//             //     allowedOriginPatterns: 'http://localhost:3000',
//             //     },
//             onConnect: () => {
//                 // Perform actions after successful connection
//                 const destination = `/topic/chat/${chatId}`; // Specify the destination for the server-side message handler
//                 client && client.subscribe(destination, (message) => {
//                     console.log('Received message:', JSON.parse(message.body));
//                     // Process the received message as needed
//                 });
//             },
//             // You can add more event handlers and configuration options as needed
//         });

//         // Connect to the WebSocket server
//         client.activate();


//         // Clean up the connection on component unmount
//         return () => {
//             client && client.deactivate();
//         };


//     }, []);

//     return <h1>Listening for WebSocket updates...


//     </h1>;
// };

// export default WebSock

const WebSock = () => {
    const { isLoggedIn } = useContext(AuthContext);
    const [messages, setMessages] = useState([])
    const [user, setUser] = useState(null)
    const [header, setHeader] = useState();
    const { ChatAPI } = WebChatApi();

    let onConnected = () => {
        console.log("Connected!!")
    }

    let onMessageReceived = (msg) => {
        console.log('New Message Received!!', msg);
        setMessages(messages.concat(msg));
        // message recieved we can send ChatApi.getMessage
    }

    let onSendMessage = (msgText) => {

        ChatAPI.sendMessage(user.username, msgText).then(res => {
            console.log('Sent', res);
            console.log("message texted", msgText)
            console.log("username", user.username)
        }).catch(err => {
            console.log('Error Occured while sending message to api');
        })
    }
    useEffect(() => {
        let getToken = null;

        if (isLoggedIn) {
            getToken = isLoggedIn?.token;
        } else {

            if (localStorage.getItem('jwt') != null) {
                let parserData = JSON.parse(localStorage.getItem('jwt'));

                getToken = parserData.headers['access-token']
            }
        }
        const headers = {
            "Accept": "application/json",
            "Content-Type": "application/json",
            "Access-Control-Allow-Origin": "*",
            "Access-Control-Allow-Methods": "POST, PUT, PATCH, GET, DELETE, OPTIONS",
            "Access-Control-Allow-Headers": "Origin, X-Api-Key, X-Requested-With, Content-Type, Accept, Authorization, Access-Token, Uid",
            "Access-Control-Expose-Headers": "*",
            "withCredentials": false,
            // "Authorization": "Bearer " + getToken
        }
        setHeader(headers);
        setUser({
            username: localStorage.getItem('uid'),
            color: "randomColor()"
        })
    }, [])
    const kj = { 9: "ccc" }
    const onError = (err) => {
        console.log(err)
    }

    let handleLoginSubmit = (username) => {
        console.log(username, " Logged in..");

        setUser({
            username: username,
            color: "randomColor()"
        })

    }
    //stompClient.connect()
    //SockJsClient.onConnect(() => header)
    //stompClient.connect(header, onConnected, onError)
    console.log("logging in websock")

    return (
        <div className="App">
            {user ? <>
                <SockJsClient
                    url={SOCKET_URL}
                    topics={['/topic/group']}
                    onConnect={onConnected}
                    onDisconnect={console.log("Disconnected!")}
                    onMessage={msg => onMessageReceived(msg)}
                    debug={true}
                // headers={{ "server": "localhost" }}
                // subScriber={{ "server": "localhost" }}

                />
                <WebSockMessage
                    messages={messages}
                    currentUser={user}
                />
                <InputText onSendMessage={onSendMessage} />
            </>
                : "<LoginComponent />"
            }

        </div >
    )
}
export default WebSock;