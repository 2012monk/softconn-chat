const freindDiv = document.getElementById('friends-list');
const infoDiv = document.getElementById('info-panel');
const mainDiv = document.getElementById('main-view');
// const userListTmpl = document.getElementById('user-list-tmpl');
const listTmpl = document.getElementById('list-tmpl');
var templates = null;

// const _URL = "http://localhost:47788/soft/";
// const _URL = "http://112.169.196.76:47788/soft/";

const _URL = "http://112.187.182.64:47788/soft/"; //home

document.querySelector('#home a').href = _URL+"conn";
document.querySelector('#logout a').href = _URL+"login/logout";



document.querySelector('nav').addEventListener('click', handleListMenu);
function handleListMenu (e) {
    if (e.target.id === 'user/list' ||
            e.target.id === 'room/list'){
        handleRoomOut();    
        fetch(`${_URL}${e.target.id}`)
            .then((res) => {
                return res.json()
            })
            .then((data) => {
                // console.log(listTmpl.innerHTML);
                if (e.target.id === 'room/list'){
                    data.room = true;
                }
                // console.log(JSON.stringify(data));

                mainDiv.innerHTML = Mustache.render(listTmpl.innerHTML, data);
            })
            .catch((err) => {
                console.log(err)
            })
        }
}


window.onload = (e) => {
    const header = new Headers();
    
    fetch(_URL+'user/info', {
        credentials:'include'
    })
        .then((res) => {
            return res.json()})
        .then((data) => {
            const json = data;
            console.log(JSON.stringify(json));
            const r = Mustache.render(document.getElementById('friend-info-tmpl').innerHTML, json);
            infoDiv.innerHTML = r;
            const g = document.getElementById('greetings');
            document.querySelector('nav').innerHTML +=
                Mustache.render(g.innerHTML, json);
            return;
        })
        .catch((e) => {
            console.log(e)
        })

    fetch(_URL+"user/friends")
        .then((res) => {
            return res.json();
        })
        .then((data) => {
            console.log(data);
            freindDiv.innerHTML =
                Mustache.render(
                    listTmpl.innerHTML, data);
        })
        .catch((e) => {
            console.log(e)
        })

    fetch(`${_URL}room/list`)
    .then((res) => {
        return res.json()
    })
    .then((data) => {
        // console.log(listTmpl.innerHTML);
        if (e.target.id === 'room/list'){
            data.room = true;
        }
        // console.log(JSON.stringify(data));

        mainDiv.innerHTML = Mustache.render(listTmpl.innerHTML, data);
    })
    .catch((err) => {
        console.log(err)
    })

}

function loadFetch (uri, method) {
    const _url = "http://localhost:47788/soft/"+uri;
    fetch(_url)
    .then((res) => {
        return res.text();
    })
    .then((json) => {
        console.log(json);
        return json;
    })

}

function userMenuHandler (e) {
    const m = document.querySelector('.return-msg');
    switch(e.target.value){
        case 'info':
            const f = new URLSearchParams();
            f.append('friendId', e.target.name);
            fetch(_URL+'user/friend-info',{
                method:'POST',
                body: f
            })
            .then((res) => {
                return res.json();
            })
            .then((data) => {
                console.log();
                console.log(JSON.stringify(data));
                infoDiv.innerHTML = 
                Mustache.render(
                    document.getElementById('user-info-tmpl').innerHTML, data);
            })
            .catch((e) => {
                console.log(e)
            })
            break;
        case 'room-info':
            const form = new URLSearchParams();
            form.append("roomId", e.target.name.split("_")[0]);
            let reqUrl = new URL(_URL+"room/info");
            reqUrl.search =  form.toString();
            console.log(reqUrl)
            fetch(reqUrl)
            .then((res) => {
                return res.json();
            })
            .then((data) => {
                infoDiv.innerHTML = 
                Mustache.render(
                    document.getElementById('room-info-tmpl').innerHTML, data);
            })
            break;
        case 'sendMsg':
            break;
        case 'addFriend':
            const friendName = e.target.name;
            const data = new URLSearchParams();
            data.append('friendId', friendName);
            
            console.log(friendName)
            // ?friendId=${friendName}
            fetch(_URL+`user/add`, {
                method:'POST',
                body:data
            })
            .then((res) => {
                return res.text();
            })
            .then((data) => {
                if (data === 'success'){
                    document.querySelector('.success-msg').classList.remove('hidden');
                }else{
                    document.querySelector('.failed-msg').classList.remove('hidden');
                }
            })
            break;
        case 'DM':
            const friendId = e.target.name;
            console.log(friendId);
            handlePrivate(friendId);
            break;
        case "enter":
            const roomId = e.target.name.split("_")[0];
            const roomName = e.target.name.split("_")[1];
            console.log(roomId);
            const tmpl = document.getElementById('chat-tmpl').innerHTML;
            mainDiv.innerHTML = Mustache.render(tmpl, {
                'roomId': roomId,
                'roomName' : roomName
            })
            document.querySelector('.msg-input').addEventListener('submit', handleMsgSend);
           
            handleEnterRoom (roomId);
            break;
        case "create-room":
            roomModView('create');
            break;
        case "delete-room":
            roomModView('delete');
            break;
        case "change-room":
            roomModView('update')
            break;
        case "search-room":
            roomModView('search');
            break;
        }

}

function roomDelHandle () {
    const p = document.querySelector('.room-mod-menu form');
    p.action(_URL+"room/delete");
}

function roomModView(mode) {
    const p = document.querySelector('.room-mod-menu');
        if (p.querySelector('ul').classList.contains('hidden')){
            p.querySelector('ul').classList.remove('hidden');
            p.querySelector('form').classList.add('hidden');
        }else{
            p.querySelector('ul').classList.add('hidden');
            p.querySelector('form').classList.remove('hidden');
        }

    if (mode !== 'search'){
        document.querySelector('.room-mod-menu form').id = 'room/'+mode;
        console.log(p.id);
        p.querySelector('form').addEventListener('submit', sendRoomMode);
    }else{
        p.querySelector('form').addEventListener('submit', roomSearch);
    }

}

function roomSearch (e) {
    e.preventDefault();
    const ul = document.querySelector("#main-view .user-list-main");
    const name = new FormData(this).get("roomName");
    const li = document.querySelector(`#main-view ul li[value="${name}"]`);
    if (li === null){
        alert('nono')
    }else{
        ul.prepend(li);
    }
}

function sendRoomMode(e) {
    e.preventDefault();
    const f = new FormData(e.target);
    console.log(f.get("roomName"));
    const url = this.id;

    const form = new URLSearchParams();
    f.forEach(function(v, k) {
        form.append(k ,v);
        console.log(k, v);
    })
    e.target.querySelector('input[type="text"]').value = "";


    console.log(form.get("roomName"));
    fetch(_URL+url, {
        method:'POST',
        body: form
    })
    .then((res) => {
        return res.text();
    })
    .then((data) => {
        if (data==='SUCCESS'){
            alert('success')
        }
    })
}



const btn = document.querySelector('.private-menu-button');
const footerPanel = document.getElementById('footer-panel');



footerPanel.addEventListener('click', (e) => {
    const btnId = e.target.id;
    const p = document.getElementById(btnId+"-panel");
    if (p.classList.contains('hidden-pop')){
        p.classList.remove('hidden-pop')
    }else {
        p.classList.add('hidden-pop')
    }
})




// mainDiv.addEventListener('click', roomModView);
mainDiv.addEventListener('click', userMenuHandler);
freindDiv.addEventListener('click', userMenuHandler);