import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import HeaderComponent from './Header/HeaderComponents.tsx';
import useKafkaApi from '../service/KafkaService.ts'
import ReadConsumerMsg from './ReadConsumerMsg.tsx'
import useGetUserList from '../service/AuthService.ts';
import { ILMsg } from './IDataType/IType.ts'
//import { ILMsg } from '../Component/IDataType/IType.ts';

/*
<div className={dt.userId != localStorage.getItem('uid') ? `senderMsg rcvr f-right ` : `senderMsg`} > {dt.message}</div>
                    } */
const ReadUserMessage = (props) => {
    console.log(props, "props")
    const { callKafkaApi } = useKafkaApi()
    const [listOfMsg, setListOfMsg] = useState<ILMsg[]>([])
    const [livechat, setLivechat] = useState([])
    const [createMsg, setCreateMsg] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [livechatId, setLiveChatId] = useState();
    const [rmChtId, setRmChtId] = useState();
    const [rvrName, setRvrName] = useState();
    const [messageType, setMessageType] = useState()
    const rcvrId = useParams()
    let recver_id = props.rcvrId ? props.rcvrId : rcvrId.id
    const { getUserList } = useGetUserList()

    useEffect(() => {
        getListOfMessage();
        getRecverUserDetails();
    }, [recver_id])

    useEffect(() => {
        if (livechatId != undefined)
            setTimeout(() => {
                liveChatIdUpd(livechatId, "load")
            }, 8000)
    }, [livechatId])
    const removeMsg = (chatId) => {
        setListOfMsg(listOfMsg.map((dt) => {
            if (dt.chatId == res) { dt.messageType = "removed"; } return dt
        }))
        callKafkaApi('/kafka-prod/chat/remove', chatId).
            then((res) => {
                console.log(res);
                setRmChtId(res)
                setListOfMsg(listOfMsg.map((dt) => {
                    if (dt.chatId == res) { dt.message = null; dt.messageType = null; } return dt
                }))
                //setUserList(res.data)
            }).catch(err => {
                console.log(err)

            })
    }
    const liveChatIdUpd = (chatId, updTyp) => {


        let getExists = listOfMsg.length > 0 && listOfMsg.filter(dt => dt['chatId'] == chatId).length > 0;

        if (getExists) {
            //sendMsg['chatId'] = res.data;
            let upList = listOfMsg.map((dt) => {
                if (dt['chatId'] == chatId) {
                    if (dt['loadStatus'] == "load" && updTyp == "load") {
                        dt['loadStatus'] = "";
                        dt['messageType'] = "received";
                    }
                    // if (updTyp == "received") {

                    //     dt['messageType'] = "received";
                    // }
                }

                return dt;
            })
            console.log(upList, "liveChatUpd   =>  " + updTyp)
            setListOfMsg(upList)
        }

    }

    useEffect(() => {

        // setLivechat([...livechat, props.liveChat])
        console.log(props.liveChat, "Chceing uefct")
        if (props.liveChat != undefined) {
            console.log("a->")

            // Reciever user recivs rhe message can be render on this check
            if ((recver_id == props.liveChat.userId || recver_id == props.liveChat.recieverId) && props.liveChat.recieverId == localStorage.getItem("uid") && props.liveChat.messageType == "received") {
                // recieving message
                console.log("c->")

                if (listOfMsg.length > 0) {
                    setListOfMsg([...listOfMsg, props.liveChat])
                } else {
                    setListOfMsg([props.liveChat])
                }


                callConsumedMsg()
            }
            let messageType = listOfMsg.length > 0 && listOfMsg.filter(dt => dt.chatId == props.liveChat.chatId).length > 0 && listOfMsg.filter(dt => dt.chatId == props.liveChat.chatId)[0].messageType;

            // sender user to notified that reciver has send
            if (props.liveChat.userId == localStorage.getItem("uid") && props.liveChat.messageType == "received" && messageType != "consumed") {
                console.log("r->")
                liveChatIdUpd(props.liveChat.chatId, props.liveChat.messageType)
            }
        }
    }, [props.liveChat?.chatId])
    useEffect(() => {


        // Sender user been got acknowledge from this check to render
        if (props.liveChat?.recieverId == recver_id && props.liveChat?.messageType == "consumed") {
            // Adding Acknowlegemnet element
            console.log("d->")
            listOfMsg.length > 0 && listOfMsg.map((l) => {
                console.log(l)
            });

            let getExists = listOfMsg.length > 0 && listOfMsg.filter(dt => dt.chatId == props.liveChat.chatId).length > 0;
            if (getExists) {
                let mapMsg = listOfMsg.map(dt => {
                    if (dt.chatId == props.liveChat.chatId) {
                        dt.messageType = props.liveChat.messageType
                        dt.loadStatus = ""
                    }
                    return dt
                })
                setListOfMsg(mapMsg)

            } else {
                if (listOfMsg.length > 0) {
                    setListOfMsg([...listOfMsg, props.liveChat])
                } else {
                    setListOfMsg([props.liveChat])
                }
                //setListOfMsg([...listOfMsg, props.liveChat])
            }
            //  listOfMsg.map((p) => p.chatId == props.liveChat.chatId ? p.messageType = props.liveChat.messageType : null)
            // debugger

            props.revertChatMsg();
            // callConsumedMsg()
        }
    }, [props.liveChat?.messageType, messageType])
    const getRecverUserDetails = () => {
        getUserList('/users-ws/users/' + recver_id).
            then((res) => {

                setRvrName(res.data.firstName)


            }).catch(err => {
                console.log(err)

            })
    }
    const getListOfMessage = () => {
        setIsLoading(true)
        let rcvId = recver_id;
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
                res.data = res.data.length > 0 && res.data.map(p => {
                    p.loadStatus = ""
                    return p;
                }
                )
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
                // console.error(err)

            })
    }
    const callSendMsg = () => {
        let sendMsg = {
            "userId": localStorage.getItem('uid'),
            "recieverId": recver_id,
            "message": createMsg
        }
        setIsLoading(true)
        callKafkaApi('/kafka-prod/chat/sendmessage', sendMsg).
            then((res) => {
                console.log(res, 'sender api response');
                setTimeout(() => {
                    let getExists = listOfMsg.length > 0 && listOfMsg.filter(dt => res.data == dt.chatId).length > 0;

                    if (!getExists && listOfMsg.length > 0) {
                        sendMsg['chatId'] = res.data;
                        sendMsg['loadStatus'] = "load";
                        setListOfMsg([...listOfMsg, sendMsg])
                        // setLiveChatId()
                    } else {

                        sendMsg['chatId'] = res.data;
                        sendMsg['loadStatus'] = "load";
                        setListOfMsg([sendMsg])
                    }
                    //setTimeout(() => { getAllUser() }, 1000)
                    setIsLoading(false)
                    setCreateMsg("")

                    setLiveChatId(res.data)


                }, 2000)
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


    const callConsumedMsg = () => {
        let sendMsg = {
            "userId": localStorage.getItem('uid'),
            "recieverId": recver_id,
            "message": createMsg
        }
        //setIsLoading(true)
        let updateConReq = { ...props.liveChat, messageType: "consumed" }
        callKafkaApi('/kafka-prod/chat/consumed', updateConReq).
            then((res) => {
                console.log(res);
                //setTimeout(() => { getAllUser() }, 1000)
                // console.log("make double tick there")
                let getExists = listOfMsg.length > 0 && listOfMsg.filter(dt => res.data == dt.chatId).length > 0;
                if (getExists) {
                    // sendMsg['chatId'] = res.data;
                    sendMsg['messageType'] = "consumed";
                    setListOfMsg([...listOfMsg, sendMsg])
                    // setLiveChatId()
                }

                // setIsLoading(false)
            }).catch(err => {
                console.error(err)
                //setIsLoading(false)

            })
    }
    const createMsgBox = () => {
        return <>

            <div className='userMsg'>

                <div className='f-right mb15px'> <button type="button" style={{ 'opacity': isLoading ? '0.5' : '1' }} disabled={isLoading} className="btn cursor pd10 f-left createBoxBtn" onClick={() => callSendMsg()}> Send Message </button></div>
                <div className='f-left mb15px '>
                    <textarea name="fstnm" className="boxBg" value={createMsg ? createMsg : ''} onChange={(event) => setCreateMsg(event.target.value)} cols="30" rows="10"></textarea>
                </div>

            </div></>
    }
    console.log(listOfMsg, 'debuger listOfMsg')
    return <>  <div className="chatBox mg15px" >
        {isLoading && (<div className="loader"></div>)}
        <div>
            <div>
                <h4 className='clearBoth uCase'>{rvrName}</h4>
                <div className='historyMsg'>
                    <ReadConsumerMsg list={listOfMsg} liveChat={props.liveChat} removeMsg={(chatId) => removeMsg(chatId)} />

                </div>
                {createMsgBox()}
                <div className="clearBoth"></div>
            </div>
        </div>
    </div></>
}
export default ReadUserMessage;