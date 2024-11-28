import { useContext, useEffect, useState } from 'react'
import useGetUserList from '../service/AuthService.ts'
import AuthContext from '../ContextData/AuthContext.ts';
import HeaderComponent from './Header/HeaderComponents.js'

function ProfileComponents() {


    const { isLoggedIn, token } = useContext(AuthContext)
    const { getUserList, createUserApi } = useGetUserList();
    const [userList, setUserList] = useState();

    useEffect(() => {
        console.log(isLoggedIn?.token)

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

    console.log(isLoggedIn, 'isLogin')
    const getAllUser = () => {
        getUserList('/users-ws/users/' + localStorage.getItem('uid')).
            then((res) => {
                console.log(res);
                setUserList(res.data)
            }).catch(err => {
                console.log(err)

            })
    }
    const useerlist = () => userList &&
        <div className='userdtls'>
            <div className='fl  mb15px'>FirstName</div>
            <div className='fr mb15px b'>{userList.firstName}</div>
            <div className='fl mb15px'>LastName</div><div className='fr mb15px b'>{userList.lastName}</div>
            <div className='fl mb15px'>Email</div><div className='fr mb15px b'>{userList.email}</div>
            <div className="clr"></div>
        </div>

    return <>
        <div className='container'>
            <div className="page">
                <HeaderComponent isPrf={true} />

                <div className='chatBox mt15px'>
                    <h4>User Profile</h4>
                    {useerlist()}
                </div>
            </div>
        </div> </>

}
export default ProfileComponents