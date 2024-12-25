import { useEffect, useState } from 'react';

function ReadConsumerMsg({ list }) {

    return <div>{
        list && list.length > 0 && list.map((dt, index) => {
            return <div key={index} className={dt.userId != localStorage.getItem('uid') ? `senderMsg rcvr f-right ` : `senderMsg`} > {dt.message}</div>
        }
        )}

    </div >
}
export default ReadConsumerMsg;