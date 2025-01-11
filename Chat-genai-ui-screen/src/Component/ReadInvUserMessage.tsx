import { useParams } from 'react-router-dom';
import HeaderComponent from './Header/HeaderComponents.js';
import UserList from './UserList.jsx';

const ReadUserMessage = (props) => {
    const rcvrId = useParams()

    return <>  <div className='container'>
        <div className="page">
            <HeaderComponent />
            <div className="chatBox mg15px">

            </div>
        </div></div></>
}
export default ReadUserMessage;