import Axios from "axios";
import { useContext } from 'react'
import AuthContext from '../ContextData/AuthContext.ts';

const api = Axios.create({
    baseURL: 'http://localhost:8082/kafka-prod/ws-chat/api',
});

const createMsg = Axios.create({
    baseURL: 'http://localhost:8082/kafka-prod/chat',
});

//  let getToken = isLoggedIn ?  :  != null ? localStorage.getItem('jwt') 

function WebChatApi() {

    const { isLoggedIn } = useContext(AuthContext);

    let getToken = null;

    if (isLoggedIn) {
        getToken = isLoggedIn?.token;
    } else {

        if (localStorage.getItem('jwt') != null) {
            let parserData = JSON.parse(localStorage.getItem('jwt'));

            getToken = parserData.headers['access-token']
        }
    }

    const headerUData = {

        headers: {
            Accept: "application/json",
            "Content-Type": "application/json",
            "Access-Control-Allow-Origin": "*",
            "Access-Control-Allow-Methods": "POST, PUT, PATCH, GET, DELETE, OPTIONS",
            "Access-Control-Allow-Headers": "Origin, X-Api-Key, X-Requested-With, Content-Type, Accept, Authorization, Access-Token, Uid",
            "Access-Control-Expose-Headers": "*",
            "withCredentials": true,
            "Authorization": "Bearer " + getToken
        },
    }

    const ChatAPI = {
        getMessages: (groupId) => {
            console.log('Calling get messages from API');
            return api.get(`messages/${groupId}`);
        },

        sendMessage: (username, text) => {
            let reqObj = {

                userId: username,
                recieverId: "recverId",
                message: text,

            }
            let msg = {
                userId: localStorage.getItem("uid"),
                recieverId: "recvrId",
                content: text
            }
            //return api.post(`send`, reqObj, headerUData);
            return createMsg.post(`sendmessage`, reqObj, headerUData);
        }
    }
    return { ChatAPI }
}

export default WebChatApi;