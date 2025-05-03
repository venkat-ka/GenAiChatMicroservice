export interface IMsg {
    chatId: String | null;
    userId: String | null;
    recieverId: String | null;
    messageId?: String | null;
    partitionId: String | null;
    message: String;
    messageType: String | null;
    loadStatus: String | null;
}
export interface ILMsg {
    LMsg: IMsg[] | never

}