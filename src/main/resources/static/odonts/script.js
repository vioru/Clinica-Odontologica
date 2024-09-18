window.addEventListener("load", () => {
  const url = "http://localhost:8080/odontologos";
  async function listar() {
    try {
      let res = await fetch(`${url}/listar`);
      //console.log("res");
      //console.log(res);

      if (!res.ok) {
        console.log("Error respuesta: " + res.ok);
        console.error(`status: ${res.status}, texto: ${res.statusText}`);
        //console.error(res);
        throw new Error(`status: ${res.status}, texto: ${res.statusText}`);
        //throw new Error(res);
      }
      let data = await res.json();
      console.log("data");
      console.log(data);
      //localStorage.setItem("jwt_todo", JSON.stringify(data.jwt));
      console.log(JSON.stringify(data.jwt));
    } catch (error) {
      console.log("ERROR manejado desde catch");
      console.log(error);
    }
    return;
  }
  listar();
  async function buscarId() {
    let id = parseInt(ingresar("Ingrese id para buscar: "));
    try {
      let res = await fetch(`${url}/${id}`);
      //console.log("res");
      //console.log(res);

      if (!res.ok) {
        console.log("Error respuesta: " + res.ok);
        console.error(`status: ${res.status}, texto: ${res.statusText}`);
        //console.error(res);
        throw new Error(`status: ${res.status}, texto: ${res.statusText}`);
        //throw new Error(res);
      }
      let data = await res.json();
      console.log("data");
      console.log(data);
      //localStorage.setItem("jwt_todo", JSON.stringify(data.jwt));
      console.log(JSON.stringify(data.jwt));
    } catch (error) {
      console.log("ERROR manejado desde catch");
      console.log(error);
    }
    return;
  }
  function cargar(url) {
    fetch(`${url}/listar`)
      .then((objetoRespuesta) => {
        console.log(objetoRespuesta);
        console.log(objetoRespuesta.status);
        console.log(objetoRespuesta.url);

        return objetoRespuesta.json();
      })
      .then((datosJs) => {
        console.log(datosJs);
      })
      .catch((error) => console.log(error));
  }
});

function consultaApi(endpoint) {
  fetch(endpoint)
    .then((objetoRespuesta) => {
      console.log(objetoRespuesta);
      console.log(objetoRespuesta.status);
      console.log(objetoRespuesta.url);

      return objetoRespuesta.json();
    })
    .then((datosJs) => {
      console.log(datosJs);
      renderizarElementos(datosJs);
    })
    .catch((error) => console.log(error));
}
