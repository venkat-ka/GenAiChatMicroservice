import { useState } from 'react'

const InputText = ({ onSendMessage }) => {

    const [createMsg, setCreateMsg] = useState();
    // let onChange = (e) => {
    //     setCreateMsg(e.target.value)
    // }

    let onSubmit = () => {
        setCreateMsg("")
        onSendMessage(createMsg);
    }

    return <div className="message-input">
        <textarea name="fstnm" className="boxBg"
            value={createMsg ? createMsg : ''}
            onChange={(event) => setCreateMsg(event.target.value)} cols="30" rows="5">

        </textarea>
        <button type="button" onClick={() => onSubmit()}>Send</button>
    </div>
}

export default InputText;