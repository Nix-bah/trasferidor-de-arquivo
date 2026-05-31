const fileInput =
document.getElementById("fileInput");

const dropZone =
document.getElementById("dropZone");

const selectFile =
document.getElementById("selectFile");

const progress =
document.getElementById("progress");

const progressText =
document.getElementById("progressText");

const speedText =
document.getElementById("speed");

const result =
document.getElementById("result");

const shareLink =
document.getElementById("shareLink");

const openBtn =
document.getElementById("openBtn");

const copyBtn =
document.getElementById("copyBtn");

selectFile.onclick = () => {
fileInput.click();
};

fileInput.addEventListener(
"change",
e => upload(e.target.files[0])
);

dropZone.addEventListener(
"dragover",
e => {
e.preventDefault();
}
);

dropZone.addEventListener(
"drop",
e => {

e.preventDefault();

upload(
e.dataTransfer.files[0]
);

}
);

function upload(file){

const data =
new FormData();

data.append(
"file",
file
);

const xhr =
new XMLHttpRequest();

const start =
Date.now();

xhr.upload.onprogress =
e => {

if(e.lengthComputable){

const percent =
Math.round(
(e.loaded/e.total)*100
);

progress.style.width =
percent+"%";

progressText.innerText =
percent+"%";

const elapsed =
(Date.now()-start)/1000;

const speed =
(e.loaded/1024/1024)/elapsed;

speedText.innerText =
speed.toFixed(2)
+" MB/s";

}

};

xhr.onload = () => {

const response =
JSON.parse(
xhr.responseText
);

result.classList.remove(
"hidden"
);

shareLink.value =
response.shareLink;

openBtn.href =
response.shareLink;

loadFiles();

};

xhr.open(
"POST",
"/api/files/upload"
);

xhr.send(data);

}

copyBtn.onclick = () => {

navigator.clipboard.writeText(
shareLink.value
);

alert("Link copiado");

};

async function loadFiles(){

const response =
await fetch(
"/api/files"
);

const files =
await response.json();

const fileList =
document.getElementById(
"fileList"
);

fileList.innerHTML = "";

files.forEach(file => {

const div =
document.createElement("div");

div.className =
"file-item";

div.innerHTML = `

<div>
${file.originalName}
</div>

<div>

<a href="/file/${file.id}">
Abrir
</a>

<button onclick="deleteFile('${file.id}')">
Excluir
</button>

</div>

`;

fileList.appendChild(div);

});

}

async function deleteFile(id){

await fetch(
"/api/files/"+id,
{
method:"DELETE"
}
);

loadFiles();

}

loadFiles();

