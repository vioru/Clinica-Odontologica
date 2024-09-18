const $inputMatricula = document.querySelector("#matricula");
const $inputNombre = document.querySelector("#nombre");
const $inputApellido = document.querySelector("#apellido");
const $formOdontologo = document.querySelector("#form-odontologo");
const btnGuardar = document.querySelector("#guardar");
const btnActualizar = document.querySelector("#actualizar");
const OdontologosUrl = "http://localhost:8080/odontologos";
const $parrafoResultado = document.querySelector("#par-resultado");
const botonNuevo = document.querySelector("[name=btn-nuevo]");
const botonListar = document.querySelector("[name=btn-listar]");
const formBusqueda = document.querySelector("#form-busqueda");
const tareaBuscar = document.querySelector("[name=btn-buscar]");

async function listarOdontologos() {
  try {
    const response = await fetch(`${OdontologosUrl}/listar`);
    // Cambia esta URL por la de tu API
    const data = await response.json();
    const tableBody = document.getElementById("odontologosTable").querySelector("tbody");

    // Limpiar la tabla antes de agregar nuevos datos
    tableBody.innerHTML = "";

    data.forEach((odontologo) => {
      const row = document.createElement("tr");
      row.innerHTML = `
                    <td data-label="ID">${odontologo.id}</td>
                    <td data-label="Matrícula">${odontologo.matricula}</td>
                    <td data-label="Nombre">${odontologo.nombre}</td>
                    <td data-label="Apellido">${odontologo.apellido}</td>
                    <td class="action-btns">
                      <button class="btn edit-btn" name="editar" data-id="${odontologo.id}">Editar</button>
                      <button class="btn delete-btn" name="borrar" data-id="${odontologo.id}">Borrar</button>
                    </td>
                `;
      tableBody.appendChild(row);
    });
  } catch (error) {
    console.error("Error al obtener los odontólogos:", error);
  }
  manejarAcciones();
}

// Llamar la función al cargar la página
document.addEventListener("DOMContentLoaded", (e) => {
  console.log("DOM cargado");
  listarOdontologos();
  console.info("Paso luego de manejarAcciones()");
});

function mostrarResultado(respuesta) {
  $parrafoResultado.textContent = "";
  $parrafoResultado.textContent = respuesta;
}
async function crearOdontologo() {
  const odontologo = {
    matricula: $inputMatricula.value,
    nombre: $inputNombre.value,
    apellido: $inputApellido.value,
  };
  const settings = {
    method: "POST",
    body: JSON.stringify(odontologo),
    headers: {
      "Content-Type": "application/json",
    },
  };
  try {
    let res = await fetch(`${OdontologosUrl}/registrar`, settings);
    if (!res.ok) {
      //console.log(res);
      console.error(res.status);
      throw new Error(res);
    }
    //console.log("res exitosa");
    //console.log(res);
    let data = await res.json();
    //console.log("data");
    //console.log(data);
    console.log(`Estado: ${res.status}, Mensaje: ${res.statusText}`);

    let respuesta = res.status === 201 ? `Odontólogo creado con exito: ${data.nombre} ${data.apellido}` : "";
    mostrarResultado(respuesta);
    setTimeout(() => {
      $parrafoResultado.classList.toggle("oculto");
    }, 2000);
    listarOdontologos();
  } catch (error) {
    console.error(error);
    mostrarResultado(error);
  }
}
$formOdontologo.addEventListener("submit", (e) => {
  e.preventDefault();
  crearOdontologo();
  e.reset;
});
formBusqueda.addEventListener("submit", (e) => {
  e.preventDefault();
  const idBuscar = document.querySelector("#busqueda").value;
  buscarPorId(idBuscar);
});
botonNuevo.addEventListener("click", (e) => {
  console.info("hizo click en la tarea Nuevo");
  $formOdontologo.classList.toggle("oculto");
  btnActualizar.classList.add("oculto");
  btnGuardar.classList.remove("oculto");
});
botonListar.addEventListener("click", (e) => {
  listarOdontologos();
});
btnActualizar.addEventListener("click", (e) => {
  actualizarOdontologo();
});
function manejarAcciones() {
  //editar
  const editButtons = document.querySelectorAll(".edit-btn");
  editButtons.forEach((button) => {
    button.addEventListener("click", (e) => {
      const userId = e.target.getAttribute("data-id");
      console.log("userId: " + userId);
      btnGuardar.classList.add("oculto");
      btnActualizar.classList.remove("oculto");
      $formOdontologo.classList.remove("oculto");
      localizarOdontologo(userId);
    });
  });

  //  borrar
  const deleteButtons = document.querySelectorAll(".delete-btn");
  deleteButtons.forEach((button) => {
    button.addEventListener("click", (e) => {
      console.log(e.target);
      console.log(button.dataset.id);
      const userId = e.target.getAttribute("data-id");
      //const userId = e.target.dataset.id;
      console.info(userId);
      const confirmDelete = confirm("¿Estás seguro de que deseas borrar este Odontólogo?");
      if (confirmDelete) {
        eliminarOdontologo(userId);
      }
    });
  });
}
tareaBuscar.addEventListener("click", (e) => {
  console.info("hizo click en la tarea Buscar");
  formBusqueda.classList.toggle("oculto");
});
async function eliminarOdontologo(id) {
  try {
    console.log(id);

    const response = await fetch(`${OdontologosUrl}/eliminar/${id}`, {
      method: "DELETE",
    });

    if (!response.ok) {
      alert("Error al borrar el Odontólogo");
      throw new Error("Error al borrar el Odontólogo: ", response.status);
    }
    mostrarResultado("Se eliminó Odontólogo con id: " + id);
    setTimeout(() => {
      $parrafoResultado.classList.toggle("oculto");
    }, 2000);
    listarOdontologos();
  } catch (error) {
    console.error("Error:", error);
  }
}
async function buscarPorId(id) {
  let data = "";
  try {
    let res = await fetch(`${OdontologosUrl}/${id}`);
    //console.log("res");
    //console.log(res);
    if (!res.ok) {
      console.log("Error respuesta: " + res.ok);
      console.error(`status: ${res.status}, texto: ${res.statusText}`);
      //console.error(res);
      throw new Error(`status: ${res.status}, texto: ${res.statusText}`);
      //throw new Error(res);
    }
    data = await res.json();
    console.log("data");
    console.log(data);
    console.log(JSON.stringify(data));
    mostrarBusqueda(data);
  } catch (error) {
    console.log("ERROR manejado desde catch");
    console.log(error);
  }
  return data;
}
async function localizarOdontologo(id) {
  try {
    console.log(id);

    const response = await fetch(`${OdontologosUrl}/${id}`, {
      method: "GET",
    });
    console.log(response);
    if (!response.ok) {
      throw new Error("Error al buscar el Odontólogo: ", response.status);
    }
    let data = await response.json();
    console.log(data);
    cargarFormulario(data);
  } catch (error) {
    console.error("Error:", error);
  }
}
async function actualizarOdontologo() {
  const id = btnActualizar.dataset.id;
  const odontologo = {
    matricula: $inputMatricula.value,
    nombre: $inputNombre.value,
    apellido: $inputApellido.value,
  };
  const settings = {
    method: "PUT",
    body: JSON.stringify(odontologo),
    headers: {
      "Content-Type": "application/json",
    },
  };
  try {
    let res = await fetch(`${OdontologosUrl}/actualizar/${id}`, settings);
    if (!res.ok) {
      console.log(res);
      console.error(res.status);
      throw new Error(res);
    }
    console.log("res exitosa");
    console.log(res);
    let data = await res.json();
    console.log("data");
    console.log(data);
    console.log(`Estado: ${res.status}, Mensaje: ${res.statusText}`);
    let respuesta =
      res.status === 200 ? `Odontólogo con ${id} actualizado con exito: ${data.nombre} ${data.apellido}` : "";
    mostrarResultado(respuesta);
    setTimeout(() => {
      $parrafoResultado.classList.toggle("oculto");
    }, 2000);
    listarOdontologos();
  } catch (error) {
    console.error(error.status);
  }
}
function cargarFormulario(odontologo) {
  console.log(odontologo);
  $inputMatricula.value = odontologo.matricula;
  $inputNombre.value = odontologo.nombre;
  $inputApellido.value = odontologo.apellido;
  btnActualizar.setAttribute("data-id", odontologo.id);
}
function mostrarBusqueda(data) {
  const odontologo = data;
  const tableBody = document.querySelector("#odontologosTable tbody");

  tableBody.innerHTML = "";

  const row = document.createElement("tr");

  row.innerHTML = `
                <td>${odontologo.id}</td>
                <td>${odontologo.matricula}</td>
                <td>${odontologo.nombre}</td>
                <td>${odontologo.apellido}</td>
                <td class="action-btns">
                    <button class="btn edit-btn" data-id="${odontologo.id}">Editar</button>
                    <button class="btn delete-btn" data-id="${odontologo.id}">Borrar</button>
                </td>
            `;

  tableBody.appendChild(row);
  manejarAcciones();
}
