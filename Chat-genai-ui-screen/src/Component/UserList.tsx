import { useContext, useEffect, useState } from 'react'
import useGetUserList from '../service/AuthService.ts'
import AuthContext from '../ContextData/AuthContext.ts';
import ReadUserMessage from './ReadUserMessage.tsx'
import { useNavigate } from 'react-router-dom';
import HeaderComponent from './Header/HeaderComponents.tsx';
import { ILMsg } from './IDataType/IType.ts'
function UserList(props) {


    const { isLoggedIn, token } = useContext(AuthContext)
    const { getUserList, createUserApi } = useGetUserList();
    const [recvrId, setRecvrId] = useState();
    const [userList, setUserList] = useState();
    const [liveLoginStatus, setLiveLoginStatus] = useState();
    const [rcvrMsg, setRcvrMsg] = useState<ILMsg>([])
    const nav = useNavigate()
    useEffect(() => {
        // setRcvrMsg(props.liveChat)
    }, [props.liveChat])
    useEffect(() => {
        setLiveLoginStatus(props.liveloginStatus)
        console.log('ren', props.liveloginStatus)
    }, [props.liveloginStatus?.liveUserId, props.liveloginStatus?.liveloginStatus])

    useEffect(() => {
        console.log(localStorage.getItem('jwt'))

        if (isLoggedIn?.token || localStorage.getItem('jwt')) {

            getAllUser()

            // getUserList('/role-ws/apis/rolebyuserid/c1d6d047-09f9-4a52-af10-0e5a0c1dcfa3').
            //     then((res) => {
            //         console.log("ROle by user");
            //         console.log(res);

            //         console.log("ROle by user End")

            //     }).catch(err => {
            //         console.log(err)

            //     })

        }
        //console.log(dc)

    }, [isLoggedIn])


    const getAllUser = () => {
        getUserList('/users-ws/users/getAll').
            then((res) => {
                console.log(res);
                setUserList(res.data)
            }).catch(err => {
                console.log(err)

            })
    }

    const useerlist = () => userList && userList.map((uDt, index) => {
        // debugger
        console.log(props.liveLoginStatus?.liveUserId)
        console.log(props.liveLoginStatus?.liveloginStatus)
        let selectClass = recvrId == uDt.userId ? "selec" : ''
        return uDt.userId != localStorage.getItem('uid') &&
            <div key={index} className='mg15px i-listItem '>
                <div className="mg11tp ml15px mr15px w15px  f-left">
                    {((uDt.loginStatus == 'y' && uDt.userId != props.liveLoginStatus?.liveUserId) || (uDt.userId == props.liveLoginStatus?.liveUserId && props.liveLoginStatus?.liveloginStatus == "y")) && <div className='livenotify'></div>}</div>
                <div className={`pd10 w280px wrap uCase cursor t-left ` + selectClass} onClick={() => setRecvrId(uDt.userId)} >{uDt.firstName}</div>
                <div className="clearBoth"></div>
            </div>
    })

    const renderTextBox = (useId) => {
        return useId && <ReadUserMessage rcvrId={useId} liveChat={props.liveChat} revertChatMsg={() => props.revertChatMsg()} />
    }
    return <div className='container'>
        <div className="page">
            <HeaderComponent />

            <div className='chatBox mt15px'>

                <h4>User List</h4>
                <div className='f-left w25per mt15px ml15px'> {useerlist()}</div>
                <div className='f-right w70per'>
                    {renderTextBox(recvrId)}

                </div>
                <div className="clearBoth"></div>

            </div>
        </div>
    </div>

}
export default UserList