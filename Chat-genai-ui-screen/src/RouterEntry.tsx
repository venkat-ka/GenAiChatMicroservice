import React, { useContext, useState } from 'react';
import './index.css';
import PrivateRoute from './Auth/PrivateRoute.js';
import SockJsClient from "react-stomp"
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import AuthContext from './ContextData/AuthContext.ts';
import { ILMsg } from './Component/IDataType/IType.ts'
import UserManagment from './Component/UserManagment.jsx';
import ProfileComponents from './Component/ProfileComponents.jsx';
import UserList from './Component/UserList.tsx';
import ReadUserMessage from './Component/ReadUserMessage.tsx';
// const Root = ({ child }) => { console.log("cccc check"); return <div>homec {child}</div> }
// const Dashboard = () => {
//     return <div> mmvnn private </div>
// }
//const Chil = () => { console.log("cccc ch"); return <div>schil</div> }

const RouterEntry = () => {
    const [messages, setMessages] = useState<ILMsg>()
    const { isLoggedIn } = useContext(AuthContext);
    const [liveUserId, setLiveUserId] = useState();
    const [liveLoginStatus, setLiveLoginStatus] = useState();
    const SOCKET_URL = 'http://localhost:8082/kafka-con/ws-chat';
    const USERSOCKET_URL = 'http://localhost:8082/users-ws/ws-chat';
    let onConnected = () => {
        console.log("Connected!!")
    }

    let onMessageReceived = (msg) => {
        console.log('New Message Received!!', msg);
        if (msg.messageType == "received") {

            setTimeout(() => {
                console.log('In time loop r Received!!', msg);
                setMessages(msg);
            }, 20000)
        } else {

            setTimeout(() => {
                console.log('In time loop consumed !!', msg);
                setMessages(msg);
            }, 2000)
        }


        // message recieved we can send ChatApi.getMessage
    }

    let onUserConnected = () => {
        console.log("User Connected!!")
    }
    let onUserMessageReceived = (msg) => {
        console.log('New User Message Received!!', msg);
        if (isLoggedIn || localStorage.getItem('jwt')) {
            // call userList
            let user = {
                liveUserId: msg.userId,
                liveloginStatus: msg.loginStatus
            }

            setLiveLoginStatus(user)
        }
        // msg.firstName;
        // msg.lastName;
        // msg.email;
        // msg.userId
        // if (msg.messageType == "received") {
        //     setTimeout(() => {
        //         console.log('In time loop Received!!', msg);
        //         setMessages(msg);
        //     }, 8000)
        // } else {
        //     setTimeout(() => {
        //         console.log('In time loop Received!!', msg);
        //         setMessages(msg);
        //     }, 16000)
        // }


        // message recieved we can send ChatApi.getMessage
    }

    let revertChatMsg = () => {
        setMessages([]);
    }
    return <>
        { // websockim
            isLoggedIn?.token || localStorage.getItem('jwt') ?
                <SockJsClient
                    url={SOCKET_URL}
                    topics={['/topic/group']}
                    onConnect={onConnected}
                    onDisconnect={console.log("Disconnected!")}
                    onMessage={msg => onMessageReceived(msg)}
                    debug={true}
                // headers={{ "server": "localhost" }}
                // subScriber={{ "server": "localhost" }}

                /> : "Socket not yet connect, So login first"
        }

        { // websockim
            !isLoggedIn?.token || localStorage.getItem('jwt') ?
                <SockJsClient
                    url={USERSOCKET_URL}
                    topics={['/topic/group']}
                    onConnect={onUserConnected}
                    onDisconnect={console.log("User Disconnected!")}
                    onMessage={msg => onUserMessageReceived(msg)}
                    debug={true}
                // headers={{ "server": "localhost" }}
                // subScriber={{ "server": "localhost" }}

                /> : "Socket not yet connect, So login first"
        }
        <Router>
            <Routes>

                <Route element={<PrivateRoute />}>

                    <Route path='/user-list' element={<UserList liveChat={messages} liveLoginStatus={liveLoginStatus} revertChatMsg={() => revertChatMsg()} />} />
                    <Route exact path="/user-list/:id" element={<ReadUserMessage liveChat={messages} revertChatMsg={() => revertChatMsg()} />} />
                    <Route exact path='/' element={<ProfileComponents />} />
                    {/* <Route path='/webchat' element={<WebSock />} /> */}
                </Route>
                <Route path='/login' element={<UserManagment />} />
            </Routes>
        </Router></>
}
const Team = () => {
    return <div>teamm</div>
}

export default RouterEntry