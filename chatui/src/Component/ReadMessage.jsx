import React from 'react'

const Messages = ({ messages, currentUser }) => {

    let renderMessage = (message) => {
        const { sender, content, color, messageType } = message;

        const messageFromMe = currentUser.username === message.sender;
        const className = messageFromMe ? "Messages-message currentUser" : "Messages-message";
        return (
            <li className={className}>
                <span
                    className="avatar"
                    style={{ backgroundColor: color }}
                />
                <div className="Message-content">
                    <div className="username">
                        {sender}
                    </div>
                    <div className="text">{content} {messageType}</div>
                </div>
            </li>
        );
    };

    return (
        <ul className="messages-list">
            {
                messages.map((msg, ind) => {
                    <div key={ind}>{renderMessage(msg)}</div>
                })}
        </ul>
    )
}
export default Messages;