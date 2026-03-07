let habits = JSON.parse(localStorage.getItem("habits")) || [];
let chart;


/* PAGE SWITCHING */

function showSignup(){

document.getElementById("loginPage").style.display="none";
document.getElementById("signupPage").style.display="block";

}

function showLogin(){

document.getElementById("signupPage").style.display="none";
document.getElementById("loginPage").style.display="block";

}


/* AUTH SYSTEM */

function signup(){

let user=document.getElementById("signupUser").value;
let pass=document.getElementById("signupPass").value;

localStorage.setItem("user",user);
localStorage.setItem("pass",pass);

alert("Account created");

showLogin();

}

function login(){

let user=document.getElementById("loginUser").value;
let pass=document.getElementById("loginPass").value;

let savedUser=localStorage.getItem("user");
let savedPass=localStorage.getItem("pass");

if(user===savedUser && pass===savedPass){

document.getElementById("loginPage").style.display="none";
document.getElementById("dashboard").style.display="block";

displayHabits();

}
else{

alert("Invalid Login");

}

}

function logout(){

document.getElementById("dashboard").style.display="none";
document.getElementById("loginPage").style.display="block";

}


/* HABIT STORAGE */

function saveHabits(){

localStorage.setItem("habits",JSON.stringify(habits));

}


/* ADD HABIT */

function addHabit(){

let name=document.getElementById("habitName").value;

if(name==="") return;

let habit={
name:name,
data:Array(30).fill(0)
};

habits.push(habit);

saveHabits();

displayHabits();

document.getElementById("habitName").value="";

}


/* UPDATE HABIT */

function updateHabit(habitIndex,dayIndex,checked){

habits[habitIndex].data[dayIndex]=checked?1:0;

saveHabits();

}


/* DISPLAY HABITS */

function displayHabits(){

let habitList=document.getElementById("habitList");

habitList.innerHTML="";

habits.forEach((habit,hIndex)=>{

let card=document.createElement("div");

card.className="habit-card";

let daysHTML='<div class="days">';

for(let i=0;i<30;i++){

daysHTML+=`
<div class="day">
<input type="checkbox"
${habit.data[i]?"checked":""}
onchange="updateHabit(${hIndex},${i},this.checked)">
</div>
`;

}

daysHTML+="</div>";

card.innerHTML=`<h3>${habit.name}</h3>`+daysHTML;

habitList.appendChild(card);

});

}


/* GRAPH */

function generateChart(){

let labels=[];
let values=[];

habits.forEach(habit=>{

let total=habit.data.reduce((a,b)=>a+b,0);

labels.push(habit.name);
values.push(total);

});

let ctx=document.getElementById("chart");

if(chart) chart.destroy();

chart=new Chart(ctx,{

type:"bar",

data:{

labels:labels,

datasets:[{

label:"Habit Completion",

data:values,

backgroundColor:"#ff4d4d"

}]

},

options:{
responsive:true,
scales:{
y:{
beginAtZero:true,
max:30
}
}
}

});

}


/* PERFORMANCE ANALYSIS */

function analyzePerformance(){

let html="";

habits.forEach(habit=>{

let completed=habit.data.reduce((a,b)=>a+b,0);

let percent=((completed/30)*100).toFixed(1);

html+=`

<h3>${habit.name}</h3>

<p>Completed Days: ${completed}/30</p>

<p>Success Rate: ${percent}%</p>

<hr>

`;

});

document.getElementById("performanceResult").innerHTML=html;

}