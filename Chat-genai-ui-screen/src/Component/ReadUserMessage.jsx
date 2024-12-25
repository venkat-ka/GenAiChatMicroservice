import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import HeaderComponent from './Header/HeaderComponents.js';
import useKafkaApi from '../service/KafkaService.ts'
import ReadConsumerMsg from './ReadConsumerMsg.jsx'
/*
<div className={dt.userId != localStorage.getItem('uid') ? `senderMsg rcvr f-right ` : `senderMsg`} > {dt.message}</div>
                    } */
const ReadUserMessage = (props) => {
    const { callKafkaApi } = useKafkaApi()
    const [listOfMsg, setListOfMsg] = useState([]);
    const [livechat, setLivechat] = useState([])
    const [createMsg, setCreateMsg] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const rcvrId = useParams()

    useEffect(() => {
        getListOfMessage();
    }, [])

    useEffect(() => {

        // setLivechat([...livechat, props.liveChat])

        console.log(props.liveChat.chatId, "rnd")
        if (props.liveChat != undefined)
            setListOfMsg([...listOfMsg, props.liveChat])
    }, [props.liveChat.chatId])

    const getListOfMessage = () => {
        setIsLoading(true)
        let rcvId = rcvrId.id;
        let uid = localStorage.getItem('uid');
        let readMessage = {
            "chatId": null,
            "userId": uid,
            "recieverId": rcvId,
            "message": null
        }
        callKafkaApi('/kafka-prod/chat/readmessage', readMessage).
            then((res) => {
                console.log(res);
                //setTimeout(() => { getAllUser() }, 1000)
                setListOfMsg(res.data)
                setIsLoading(false)
            }).catch(err => {
                console.error(err)
                setIsLoading(false)
                if (err.response.status == 500) {
                    alert("email already exists")
                }
                //setTimeout(() => { getAllUser() }, 1000)
                console.error(err)

            })
    }
    const callSendMsg = () => {
        let sendMsg = {
            "userId": localStorage.getItem('uid'),
            "recieverId": rcvrId.id,
            "message": createMsg
        }
        setIsLoading(true)
        callKafkaApi('/kafka-prod/chat/sendmessage', sendMsg).
            then((res) => {
                console.log(res);
                //setTimeout(() => { getAllUser() }, 1000)
                setIsLoading(false)
                setCreateMsg("")
                //getListOfMessage()
            }).catch(err => {
                console.error(err)
                setIsLoading(false)
                if (err.response.status == 500) {
                    alert("email already exists")
                }
                setCreateMsg("")
                //setTimeout(() => { getAllUser() }, 1000)
                console.error(err)

            })
    }
    const createMsgBox = () => {
        return <>
            <div className='userdtls'>
                <div className='fr mb15px'> <button type="button" className="btn pd10 f-left createBoxBtn" onClick={() => callSendMsg()}> Send Message </button></div>
                <div className=' mb15px '>
                    <textarea name="fstnm" className="boxBg" value={createMsg ? createMsg : ''} onChange={(event) => setCreateMsg(event.target.value)} cols="30" rows="5"></textarea>
                </div>

            </div></>
    }
    console.log(livechat, props.liveChat)
    console.log(props.liveChat.chatId)
    return <>  <div className='container' style={{ 'opacity': isLoading ? '0.5' : '1' }}>
        <div className="page">
            <HeaderComponent />

            <div className="chatBox mg15px">
                <div className='historyMsg'>
                    <ReadConsumerMsg list={listOfMsg} liveChat={props.liveChat} />

                </div>
                {createMsgBox()}
                <div className="clearBoth"></div>
            </div>
        </div>
    </div></>
}
export default ReadUserMessage;