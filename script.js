// Course Outcome 4: Storage & Async Programming (Browser storage)
// Course Outcome 3: Objects & Arrays
let habits = JSON.parse(localStorage.getItem("habits")) || []
let users = JSON.parse(localStorage.getItem("users")) || []

// Course Outcome 3: JavaScript Fundamentals (Functions)
function signup(){
// Course Outcome 4: DOM & Events (DOM manipulation)
// Course Outcome 5: Handling user input dynamically
let u=document.getElementById("signupUser").value
let p=document.getElementById("signupPass").value

users.push({u,p})
localStorage.setItem("users",JSON.stringify(users))

alert("Signup successful")
}

function login(){
let u=document.getElementById("loginUser").value
let p=document.getElementById("loginPass").value

// Course Outcome 3: JavaScript Fundamentals (Arrow functions, Callback functions)
let found = users.find(x=>x.u==u && x.p==p)

// Course Outcome 3: JavaScript Fundamentals (Conditions)
if(found){
document.getElementById("auth").style.display="none"
document.getElementById("app").style.display="block"
render()
}else{
alert("Invalid login")
}
}

function logout(){
// Course Outcome 4: DOM & Events (DOM manipulation)
location.reload()
}

function addHabit(){
let name=document.getElementById("habitInput").value

if(name=="") return

// Course Outcome 3: Objects & Arrays (Array creation)
let data = new Array(30).fill(false)

habits.push({name,data})
localStorage.setItem("habits",JSON.stringify(habits))
render()
}

function deleteHabit(i){
// Course Outcome 3: Objects & Arrays (Array methods)
habits.splice(i,1)
localStorage.setItem("habits",JSON.stringify(habits))
render()
}

function updateHabit(hIndex,dIndex,value){
if(!habits[hIndex].data){
habits[hIndex].data = new Array(30).fill(false)
}

habits[hIndex].data[dIndex]=value
localStorage.setItem("habits",JSON.stringify(habits))

renderGraph()
analysis()
}

function render(){
let list=document.getElementById("habitList")
list.innerHTML=""

// Course Outcome 3: Objects & Arrays (Array methods)
habits.forEach((habit,hIndex)=>{

if(!habit.data){
habit.data=new Array(30).fill(false)
}

let daysHTML=""

// Course Outcome 3: JavaScript Fundamentals (Loops)
for(let i=0;i<30;i++){

let checked = habit.data[i] ? "checked" : ""

daysHTML+=`
<div class="day">
<input type="checkbox" ${checked}
onchange="updateHabit(${hIndex},${i},this.checked)">
</div>
`
}

list.innerHTML+=`
<div class="habit">
<button class="delete" onclick="deleteHabit(${hIndex})">Delete</button>
<h3>${habit.name}</h3>
<div class="days">
${daysHTML}
</div>
</div>
`
})

renderGraph()
analysis()
}

function renderGraph(){
let ctx=document.getElementById("progressChart")

if(!ctx) return

let data=habits.map(h=>h.data.filter(x=>x).length)
let labels=habits.map(h=>h.name)

if(window.chart){
window.chart.destroy()
}

window.chart=new Chart(ctx,{
type:"bar",
data:{
labels:labels,
datasets:[{
label:"Completed Days",
data:data
}]
}
})
}

function analysis(){
let text=""

habits.forEach(h=>{
// Course Outcome 3: JavaScript Fundamentals (Basic expressions and operators)
let done=h.data.filter(x=>x).length
let percent=Math.round((done/30)*100)
text+=`<p>${h.name} : ${percent}% consistency</p>`
})

// Course Outcome 4: DOM & Events (DOM manipulation)
document.getElementById("analysis").innerHTML=text
}

render()