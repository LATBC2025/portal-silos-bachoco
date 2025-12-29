export class OtpRequest{
    otp:string;
    username:string;
    constructor(otp:string,username:string){
        this.otp=otp;
        this.username=username;
    }
}