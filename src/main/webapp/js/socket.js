const SOCKET_URL = window.CONFIG.SOCKET_URL;
const socket = new WebSocket(SOCKET_URL);


const msgForm = {
    online(id) {
        return id + "entered greetings!" 
    },

    offline(id) {
        return id + "just left bye!"
    },

    roomUpdate(json){
        const id = json.roomId;
        try{
            document.querySelector('#roomid'+id+' .online-user-number')
            .innerText = "current user : " + json.currentUserNumber
        }catch(error){}
    }


}

socket.onopen = (e) => {
    // socket.send("hello");
}

socket.onmessage = (e) => {
    const json = JSON.parse(e.data);
    console.log(json);
    const li = document.createElement('li');
    li.innerText = json.senderId + "  :  " + json.message;
    if (json.type === "ROOM_MSG"){
        
        document.querySelector('.msg-area ul').appendChild(li);
    }

    if (json.type === "PRIVATE_MSG") {
        const ul = document.querySelector(`#p-${json.senderId}-panel ul`);
        if (ul === null){
            handlePrivate(json.senderId);
            ul.classList.remove('hidden-pop')
        }
        ul.appendChild(li);
    }
    
    if (json.type === "SEND_SUCCESS") {
        const id = `p-${json.receiverId}-panel`;
        const div = document.getElementById(id);
        // const ul = div.querySelector('ul');
        const ul = document.querySelector(`#p-${json.receiverId}-panel ul`);
        li.innerText = json.message;
        li.style.textAlign = 'right';
        ul.appendChild(li);
    }

    if (json.type.includes("NOTIFY")){
        let msg = "";
        if (json.type === "NOTIFY_ONLINE") {
            msg = msgForm.online(json.userId);
        }

        if (json.type === "NOTIFY_OFFLINE") {
            msg = msgForm.offline(json.userId);
        }

        if (json.type === "NOTIFY_ROOM"){
            msgForm.roomUpdate(json);
        }
        console.log(JSON.stringify(json)+"NOTIFY");
        document.querySelector('.return-msg').innerText = msg;
        
        document.querySelector('.return-msg').classList.remove('disappear');

        const interval=setTimeout(function(){
            document.querySelector('.return-msg').classList.add('disappear');
        }, 5000)

    }
}

function handleRoomOut () {
    if (document.querySelector('.msg-area form span') !== null){
            const json = {
                'type' : 'ROOM_OUT',
                'roomId' : document.querySelector('.msg-area form').id
            }
        // }
        socket.send(JSON.stringify(json));
        }
}


function handleMsgSend (e) {
    e.preventDefault();
    if (e.target.querySelector('.text-input').value !== ""){
        const form = new FormData(e.target);
        const json = {};
        form.forEach(function(v, k){
            json[k] = v;
        })
        console.log(JSON.stringify(json));
        socket.send(JSON.stringify(json));
        console.log(e.target.querySelector('.text-input'));
        e.target.querySelector('.text-input').value = "";
    }
}

function handleMainMsg (e) {
    e.preventDefault();
    const recevierId = e.target.id;
    const form = new FormData(e.target);
    const json = {};
    form.forEach(function(v, k){
        json[k] = v;
    })
    console.log(JSON.stringify(json));
    socket.send(JSON.stringify(json));
    e.target.querySelector('.text-input').value = "";
}

function handlePrivate (friendId) {
    if (document.getElementById('p-'+friendId) === null){
        const privatePanelTmpl = document.getElementById('private-chat-tmpl').innerHTML;
        const privateButtonTmpl = document.getElementById('private-button-tmpl').innerHTML;

        const privatePanel = document.getElementById('popup-panel');
        const privateButtonPanel = document.getElementById('footer-panel');

        const rT = Mustache.render(privatePanelTmpl, {"friendId" : friendId});
        console.log(rT)
        privatePanel.innerHTML += rT;
        privateButtonPanel.innerHTML += Mustache.render(privateButtonTmpl, {"friendId" : friendId});
        document.querySelector('.close-private').addEventListener('click', privateClose);
    }
}


function handlePopUp (e) {
}

function privateClose() {
    const id = this.value;
    document.getElementById(id).remove();
    document.getElementById(id+"-panel").parentNode.remove();
}

function handleEnterRoom (roomId) {
    const msg = {
        'roomId' : roomId,
        'type' : 'ROOM_ENTER'
    }
    socket.send(JSON.stringify(msg));
}

document.querySelector('#popup-panel').addEventListener('submit', handleMsgSend);
// mainDiv.addEventListener('submit', handleMsgSend);
// document.querySelector('footer-panel').addEventListener('click', handlePopUp);

// document.querySelector('')

window.addEventListener('beforeunload', function(e) {
    e.preventDefault();
    const msg = {
        'roomId' : roomId,
        'type' : 'ROOM_OUT'
    }
    socket.send(JSON.stringify(msg));
})
