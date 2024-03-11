
function initSession(){
    $jsapi.startSession();
    
    var session = $jsapi.context().session;
    var request = $jsapi.context().request;
    session.clientID = 0;
    if(request.rawRequest.originateData){
        session.clientID = request.rawRequest.originateData.payload.clientID || 0;
    }
    session.agree = false;
    session.success = false;
    session.fallbackCounter = 0;
    session.noInputCounter = 0;
}

function onHangUp(){
    var session = $jsapi.context().session;
    if(session.success){
        var url = 'http://34.168.220.204/call-result';
        var options = {
            dataType: "json",
            headers: {
                "Content-Type": "application/json"
            },
            body: {
                "clientID": session.clientID,
                "agree": session.agree,
                "callRecordingURL": $dialer.getCallRecordingFullUrl(),
            }
        };
        $http.post(url, options);
    }
}