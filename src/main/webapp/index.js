const url = "http://localhost:8080/check/core/"

let loginbtn = document.getElementById("loginBtn");
let pendreqbtn = document.getElementById("pendreqBtn");

loginbtn.addEventListener("click", login);
pendreqbtn.addEventListener("click", getPendingRequests);

async function login() {
    let uName = document.getElementById("username").value;
    let pWord = document.getElementById("password").value;

    let user = {
        username: uName,
        password: pWord
    };

    let response = await fetch(url + "login", {
        method: "POST",
        body: JSON.stringify(user),
        credentials: "include"
    });

    if (response.status === 200) {
        revealDivs();
        console.log("Login successful");
        console.log(response);
    }
    else {
        console.log("Could not log in");
        console.log(response);
    }
}

function revealDivs() {
    let divs = document.getElementsByTagName("div");
    console.log("You are inside");

    for (let div of divs) {
        div.hidden = false;
    }
}

async function getPendingRequests() {
    let response = await fetch(url + "reimbursement", {
        credentials: "include"
    });

    if (response.status === 200) {
        let list = await response.json();
        populatePendingRequestsTable(list);
    }
}

function populatePendingRequestsTable(list) {
    let tableBody = document.getElementById("getPendReq");
    tableBody.innerHTML = "";
    for (let req of list) {
        let row = document.createElement("tr");
        let reqId = document.createElement("td");
        let reqAuthor = document.createElement("td");
        let reqSubmissionDate = document.createElement("td");
        let reqAmount = document.createElement("td");
        let reqType = document.createElement("td");
        let reqDescription = document.createElement("td");

        reqId.innerText = req.id;
        reqAuthor.innerText = req.author.firstName + " " + req.author.lastName;
        reqSubmissionDate.innerText = req.submissionDate;
        reqAmount.innerText = req.amount;
        reqType.innerText = req.type;
        reqDescription.innerText = req.description;

        row.appendChild(reqId);
        row.appendChild(reqAuthor);
        row.appendChild(reqSubmissionDate);
        row.appendChild(reqAmount);
        row.appendChild(reqType);
        row.appendChild(reqDescription);
        tableBody.appendChild(row);
    }
}