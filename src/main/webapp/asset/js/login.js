const _URL = window.CONFIG.LOGIN_URL;

const form = document.querySelector('form');
const input = document.getElementById('id-check-form');

form.action = _URL+"/signin";
let isChecked = true;   
let id = null;

const checkdId = [];


const submitBtn = document.getElementById("sub-button");
const checkBtn = document.getElementById('id-check');
const div = document.getElementById('sign-button')


checkBtn.addEventListener('click',idOverlapCheck);
form.addEventListener('submit', submitForm);
div.addEventListener('click', (e) => {
//     div.innerText ='123';
    e.preventDefault();
    // change to sign up
    if (div.value === 'SIGNIN') {
        div.value = 'LOGIN';
        div.innerText = 'Log in';
        form.action = _URL+"/signin";
        submitBtn.value = 'SIGN UP';
        checkBtn.classList.remove('hidden');
    }else {
        // change to login
        div.value = 'SIGNIN';
        form.action = _URL+"/login";
        div.innerText = "go Sign up";
        submitBtn.value = "LOGIN";
        checkBtn.classList.add('hidden');
    }
})

function idOverlapCheck(e) {
    e.preventDefault();
    id = input.value;
    console.log(id);
    if (id === null || id === ''){
        alert('아이디를 입력하세요!');
        return;
    }
    const f = new URLSearchParams()
    f.append("userId", input.value);

    // console.log(_url);
    fetch(_URL+"/check", {
        method:'POST',
        body:f
    })
        .then((res) => {
        return res.text();
    })
    .then((data) => {
        if (data === 'ID_OVERLAP') {
            alert('중복된 아이디입니다.');
            input.value = "";
        }else{
            isChecked = true;
            alert('사용가능합니다!');
            isChecked = true;
            // input.value = id;
            checkdId.push(id);
        }
    })

    const req = new XMLHttpRequest();

    // req.onreadystatechange = (e) => {
    //     if (req.readyState === XMLHttpRequest.DONE){
    //         if (req.status === 200) {
    //             const data = req.responseText;
    //             if (data === 'overlap') {
    //                 alert('중복된 아이디입니다.');
    //                 input.value = "";
    //             }else{
    //                 isChecked = true;
    //                 alert('사용가능합니다!');
    //                 isChecked = true;
    //                 // input.value = id;
    //                 checkdId.push(id);
    //             }
    //         }
    //     }
    // }
    // req.open('POST', _URL);
    // req.setRequestHeader('Content-Type', 'text/html');
    // req.send(`userid=${id}`);
}

function submitForm (e) {
    e.preventDefault();
    if (submitBtn.value === 'LOGIN'){
        asyncLogin(e);

    }else if (checkdId.includes(input.value)){
        form.submit();


    }else{
        alert('중복확인 하세요!');
    }
}
function asyncLogin (e) {
    e.preventDefault();
    const f = new FormData(e.target);
    const form = new URLSearchParams();
    f.forEach(function(v, k) {
        form.append(k, v);
        console.log(k, v);
    })
    fetch(_URL+"/auth", {
        method: 'POST',
        credentials: 'include',
        body: form
    })
    .then((res) => {
        return res.text();
    })
    .then((data) => {
        if (data === 'NOT_VERIFIED'){
            document.
                querySelector('.login-alert').
                classList.
                remove('hidden');
        }else{
            location.reload();
        }
    })
    .catch((err) =>{
        console.log(err);
    })

}

// function asyncLogin (e) {
//     e.preventDefault();
//     const val = $("#form-login").serialize();

//     $.ajax({
//         url: _URL+"user/login",
//         type:'POST',
//         data: val,
//         success:function(data) {
//             if (data === 'FAILED') {
//                 document.
//                 querySelector('.login-alert').
//                 classList.
//                 remove('hidden');
//             }else {
//                 // window.location.href = "http://localhost:47788/soft/conn";
//                 location.reload();
//             }
//         },
//         error:function(xhr, status){
//             console.log(xhr, status);
//         }
//     })
 

// }
