import React, { useContext, useState } from 'react';
import './index.css';
import PrivateRoute from './Auth/PrivateRoute.js';
import SockJsClient from "react-stomp"
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import AuthContext from './ContextData/AuthContext.ts';

import UserManagment from './Component/UserManagment.jsx';
import ProfileComponents from './Component/ProfileComponents.jsx';
import UserList from './Component/UserList.jsx';
import ReadUserMessage from './Component/ReadUserMessage.jsx';
const Root = ({ child }) => { console.log("cccc check"); return <div>homec {child}</div> }
const Dashboard = () => {
    return <div> mmvnn private </div>
}
const Chil = () => { console.log("cccc ch"); return <div>schil</div> }
interface IMsg {
    chatId: String | null;
    userId: String | null;
    recieverId: String | null;
    message: String;
    messageType: String | null;
}
interface ILMsg {
    LMsg: [IMsg]

}
const RouterEntry = () => {
    const [messages, setMessages] = useState<ILMsg>([])
    const { isLoggedIn } = useContext(AuthContext);
    const SOCKET_URL = 'http://localhost:8082/kafka-con/ws-chat';
    let onConnected = () => {
        console.log("Connected!!")
    }

    let onMessageReceived = (msg) => {
        console.log('New Message Received!!', msg);
        setMessages(msg);
        // message recieved we can send ChatApi.getMessage
    }
    // read message will trigger
    // Connect websocket 
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
        <Router>
            <Routes>

                <Route element={<PrivateRoute />}>

                    <Route path='/user-list' element={<UserList />} />
                    <Route exact path="/user-list/:id" element={<ReadUserMessage liveChat={messages} />} />
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