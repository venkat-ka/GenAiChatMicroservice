import { useContext, useEffect, useState } from 'react'
import useGetUserList from '../service/AuthService.ts'
import AuthContext from '../ContextData/AuthContext.ts';
const UserDetailComponent = () => {
    const { isLoggedIn, token } = useContext(AuthContext)
    const { getUserList, createUserApi } = useGetUserList();
    const [userList, setUserList] = useState();
    const [userCreate, setUserCreate] = useState();

    const getAllUser = () => {
        getUserList('/users-ws/users/getAll').
            then((res) => {
                console.log(res);
                setUserList(res.data)
            }).catch(err => {
                console.log(err)

            })
    }
    useEffect(() => {
        console.log(isLoggedIn?.token)

        if (isLoggedIn?.token) {

            getAllUser()

            getUserList('/role-ws/apis/rolebyuserid/c1d6d047-09f9-4a52-af10-0e5a0c1dcfa3').
                then((res) => {
                    console.log("ROle by user");
                    console.log(res);

                    console.log("ROle by user End")

                }).catch(err => {
                    console.log(err)

                })

        }
        //console.log(dc)

    }, [isLoggedIn])


    const renderTitle = () => {
        return <h4>User Managment</h4>
    }

    const useerlist = () => userList && userList.map((uDt, index) =>
        <div key={index} className='userdtls'>
            <div className='fl  mb15px'>FirstName</div>
            <div className='fr mb15px b'>{uDt.firstName}</div>
            <div className='fl mb15px'>LastName</div><div className='fr mb15px b'>{uDt.lastName}</div>
            <div className='fl mb15px'>Email</div><div className='fr mb15px b'>{uDt.email}</div>
            <div className="clr"></div>
        </div>)

    const createUser = () => {
        return <>
            <div className='userdtls'>
                <div className='fl mb15px'>FirstName</div>
                <div className='fr mb15px b'><input type="text" name="fstnm" value={userCreate?.firstName ? userCreate.firstName : ''} onChange={(event) => setUserCreate({ ...userCreate, firstName: event.target.value })} /></div>
                <div className='fl mb15px '>LastName</div><div className='fr b mb15px'><input type="text" name="lstnm" value={userCreate?.lastName ? userCreate.lastName : ''} onChange={(event) => setUserCreate({ ...userCreate, lastName: event.target.value })} /></div>
                <div className='fl mb15px'>Email</div><div className='fr mb15px b'><input type="text" name="eml" onChange={(event) => setUserCreate({ ...userCreate, email: event.target.value })} /></div>
                <div className='fl mb15px'>Password</div><div className='fr mb15px b'><input type="password" name="psw" onChange={(event) => setUserCreate({ ...userCreate, password: event.target.value })} /></div>
                <div className="clr"></div>
            </div></>
    }


    const callCreateApi = () => {
        setUserList([])
        createUserApi('/users-ws/users', userCreate).
            then((res) => {
                alert("User registered successfully")
                //setTimeout(() => { getAllUser() }, 1000)

            }).catch(err => {
                console.error(err)

                if (err.response.status == 500) {
                    alert("email already exists")
                }
                //setTimeout(() => { getAllUser() }, 1000)
                console.error(err)

            })
    }


    const saveUser = () => {
        if (userCreate.firstName && userCreate.lastName && userCreate.email) {
            callCreateApi();
        } else {
            alert('Fields are missing')
        }
    }
    const addUser = () => {
        return <div className='w100, clearBith'>
            <button type="button" className="btn pd10 mr15px" onClick={() => saveUser()}>Create User </button></div>
    }
    return <>
        {createUser()}
        {addUser()}
    </>

}
export default UserDetailComponent;