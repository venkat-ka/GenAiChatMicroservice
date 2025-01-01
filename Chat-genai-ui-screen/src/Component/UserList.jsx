import { useContext, useEffect, useState } from 'react'
import useGetUserList from '../service/AuthService.ts'
import AuthContext from '../ContextData/AuthContext.ts';

import { useNavigate } from 'react-router-dom';
import HeaderComponent from './Header/HeaderComponents.js';

function UserList() {


    const { isLoggedIn, token } = useContext(AuthContext)
    const { getUserList, createUserApi } = useGetUserList();
    const [userList, setUserList] = useState();
    const [rcrUid, setRcrUid] = useState();
    const nav = useNavigate()
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
        return uDt.userId != localStorage.getItem('uid') &&
            <div key={index} className='userdtls'>
                <div className='fl mb15px b'>{uDt.firstName}</div>
                <div>
                    <button type='button' className="btn pd10 mr15px" onClick={() => nav('/user-list/' + uDt.userId)}>Start Chat</button></div>
            </div>
    })

    return <div className='container'>
        <div className="page">
            <HeaderComponent />

            <div className='chatBox mt15px'>

                <h4>User List</h4> {useerlist()}
            </div>
        </div>
    </div>

}
export default UserList