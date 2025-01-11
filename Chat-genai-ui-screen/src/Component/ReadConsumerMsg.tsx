import { useEffect, useState } from 'react';
import '../loader.css';
function ReadConsumerMsg({ list, removeMsg, liveChat }) {
    const msgStatus = (msg) => {
        console.log(liveChat, "liveChat")
        //loadStatus
        let sChecClass = msg?.messageType == "consumed" || msg?.messageType == "received" ? msg?.messageType == "received" ? msg?.messageType == "received" && msg?.chatId == liveChat?.chatId ? "gcheckmark draw checLoad" : "gcheckmark draw" : "ncheckmark draw chbrd" : "ncheckmark draw";
        return <div className={msg?.messageType == "consumed" || msg?.messageType == "received" || msg?.loadStatus == "" ? "f-right circle-loader load-complete" : "f-right circle-loader "}>
            <div>
                <div className="checkmark draw" style={{ "display": msg?.messageType == "consumed" || msg?.messageType == "received" ? "block" : "none" }} ></div>
                <div className={` ` + sChecClass} style={{ "display": msg.messageType == "consumed" || msg?.messageType == "received" ? "block" : "none" }} ></div>
            </div>
        </div >
    }

    return <div className={'l_hmsg'}>{
        list && list.length > 0 && list.map((dt, index) => {
            //return <>cc</>
            // { liveChat }
            let isRmCls = dt.messageType == "removed" ? "rmvLoad" : ""
            return <div key={index} className={isRmCls}>
                <div className={dt?.userId != localStorage.getItem('uid') ? `senderMsg rcvr f-right ` : `senderMsg`} > {dt?.message}   {dt?.messageType}
                    {dt.userId == localStorage.getItem('uid') && msgStatus(dt)}
                </div>
                {dt?.userId == localStorage.getItem('uid') && dt?.message != null && dt?.messageType == "removed" && (<div className='delete-icon f-left' onClick={() => removeMsg(dt)}></div>)}
            </div>
        }
        )}

    </div >
}
export default ReadConsumerMsg;