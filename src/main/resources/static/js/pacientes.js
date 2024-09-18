
document.addEventListener('DOMContentLoaded', () => {
    const apiUrl = 'http://localhost:8080/pacientes'; 
    const hiddenElement = document.querySelector('#form-hidden'); 
    const saveButton = document.getElementById('guardar');
    let agreeOrEdit = 'add';
    let editOnePaciente = 'edit';
    const form = document.getElementById('pacientForm');
    const findForm = document.getElementById('form-busqueda');
    const buttonBack = document.getElementById('btn-back');
    const messageUpdate = document.getElementById('message-update');
    const agreeButton = document.querySelector('.agree-btn');


    // api lista los pacientes
    const fetchDataList = async () => {
        try {
            const response = await fetch(`${apiUrl}/listar`);
            if (!response.ok) {
                throw new Error('Error al obtener los datos');
            }
            const data = await response.json();
            console.log(data);
            renderTable(data);
        } catch (error) {
            console.error('Error:', error);
        }
    };

    // la tabla se llena con los datos de la api-listar pacientes
    const renderTable = (data) => {
        const usuarios = data;
        const tableBody = document.querySelector('.table-pacients tbody');
        tableBody.innerHTML = '';
        usuarios.forEach(usuario => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${usuario.nombre}</td>
                <td>${usuario.apellido}</td>
                <td>${usuario.dni}</td>
                <td>${usuario.domicilioSalidaDto.calle} , ${usuario.domicilioSalidaDto.numero} ${usuario.domicilioSalidaDto.localidad} - ${usuario.domicilioSalidaDto.provincia}</td>
                <td class= "action-btns">
                    <button class="edit-btn" data-id="${usuario.id}">Editar</button>
                    <button class="delete-btn" data-id="${usuario.id}">Borrar</button>
                </td>
            `;
            tableBody.appendChild(row);
        });
        addEventListeners();
    };

    const addEventListeners = () => {
        const findButton = document.getElementById('find-Id');
        if (findButton) {
            findButton.removeEventListener('click', findButtonClickHandler); 
            findButton.addEventListener('click', findButtonClickHandler);
        }
    
        const agreeButton = document.querySelector('.agree-btn');
        if (agreeButton) {
            agreeButton.removeEventListener('click', agreeButtonClickHandler); 
            agreeButton.addEventListener('click', agreeButtonClickHandler);
        }
    
        const editButtons = document.querySelectorAll('.edit-btn');
        editButtons.forEach(button => {
            button.removeEventListener('click', editButtonClickHandler); 
            button.addEventListener('click', editButtonClickHandler);
        });
    
        const deleteButtons = document.querySelectorAll('.delete-btn');
        deleteButtons.forEach(button => {
            button.removeEventListener('click', deleteButtonClickHandler); 
            button.addEventListener('click', deleteButtonClickHandler);
        });
    
        const saveButton = document.getElementById('guardar');
        if (saveButton) {
            saveButton.removeEventListener('click', saveButtonClickHandler); 
            saveButton.addEventListener('click', saveButtonClickHandler);
        }
    };
    
    

    const findButtonClickHandler = (e) => {
        e.preventDefault();
        const idPacient = document.getElementById('idBusqueda').value;
        if (!idPacient) {
            hiddenElement.classList.add("form-hidden");
            alert("Ingresa un ID válido");
            fetchDataList();
        } else {
            editOnePaciente = "find";
            fetchData(idPacient);
            hiddenElement.classList.add("form-hidden");
        }
        findForm.reset();
    };


    const agreeButtonClickHandler = (e) => {
        e.preventDefault();
        hiddenElement.classList.remove("form-hidden");
        form.reset();
        agreeOrEdit = 'add';
        const h2Element = document.querySelector('.edit-user-name');
        h2Element.textContent = `Agregar un nuevo paciente`;
        const textP = document.querySelector('.textP');
        textP.textContent = "Debes ingresar correctamente todos los datos, ningún campo debería quedar vacío";

        const errorsContainer = document.querySelector('.errors');
        errorsContainer.innerHTML = '';
    };

    const editButtonClickHandler = (e) => {
        const userId = e.target.getAttribute('data-id');
        hiddenElement.classList.remove("form-hidden");
        editOnePaciente = "edit";
        agreeOrEdit = 'update';
        fetchData(userId);
    };

    const deleteButtonClickHandler = (e) => {
        const userId = e.target.getAttribute('data-id');
        const confirmDelete = confirm('¿Estás seguro de que deseas borrar este usuario?');
        if (confirmDelete) {
            deleteUser(userId);
        }
    };

    const saveButtonClickHandler = (event) => {
        event.preventDefault();
        if (agreeOrEdit === 'add') {
            newPacient(event);
        } else if (agreeOrEdit === 'update') {
            updateUserData(event);
        }
    };

    const deleteUser = async (id) => {
        try {
            console.log(id);
            const response = await fetch(`${apiUrl}/eliminar/?id=${id}`, {
                method: 'DELETE'
            });
            if (!response.ok) {
                alert('Error al borrar el usuario');
                throw new Error('Error al borrar el usuario');
            }
            console.log("haz fetch");
            
            fetchDataList();
        } catch (error) {
            console.error('Error:', error);
        }
    };

    const fetchData = async (user) => {

        try {
            const response = await fetch(`${apiUrl}/${user}`);
            if (!response.ok) {
                throw new Error('Error al obtener los datos');
            }
            const data = await response.json();
            const hiddenElement = document.querySelector('.form-hidden');
            if (hiddenElement) {
                hiddenElement.classList.remove('.form-hidden');
            }
            if (editOnePaciente === "edit") {
                chargeUserDataEdit(data);
            } else if (editOnePaciente === "find") {
                userFindData(data);
            }
        } catch (error) {
            alert(`paciente con id # ${user} no se encontró`);
            console.log("aqui ", user);
            console.error('Error:', error);
        }
    };

    const userFindData = (user) => {
        console.log("el user que voy a renderizar despues de buscar ");
        console.log(user);
        buttonBack.classList.remove("hidden");
        const tableBody = document.querySelector('.table-pacients tbody');
        if (tableBody) {
            tableBody.innerHTML = '';
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${user.nombre}</td>
                <td>${user.apellido}</td>
                <td>${user.dni}</td>
                <td>${user.domicilioSalidaDto.calle} , ${user.domicilioSalidaDto.numero} ${user.domicilioSalidaDto.localidad} - ${user.domicilioSalidaDto.provincia}</td>
                <td class= "action-btns">
                    <button class="delete-btn" data-id="${user.id}">Borrar</button>
                </td>
            `;
            tableBody.appendChild(row);
        } else {
            console.error("No se encontró tbody en la tabla de pacientes.");
        }
        addEventListeners();
    };

    const chargeUserDataEdit = (user) => {
        const nombreUsuario = user.nombre + " " + user.apellido;
        const h2Element = document.querySelector('.edit-user-name');
        h2Element.textContent = `Editando la información del paciente: ${nombreUsuario}`;
        const textP = document.querySelector('.textP');
        textP.textContent = "Debes ingresar correctamente todos los datos, ningun campo deberia quedar vacio";
        document.getElementById('id').value = user.id;
        document.getElementById('nombre').value = user.nombre;
        document.getElementById('apellido').value = user.apellido;
        document.getElementById('dni').value = user.dni;
        document.getElementById('calle').value = user.domicilioSalidaDto.calle;
        document.getElementById('numero').value = user.domicilioSalidaDto.numero;
        document.getElementById('localidad').value = user.domicilioSalidaDto.localidad;
        document.getElementById('provincia').value = user.domicilioSalidaDto.provincia;
    };

    const newPacient = async (event) => {
        event.preventDefault();
        const nombre = document.getElementById('nombre').value;
        const apellido = document.getElementById('apellido').value;
        const dni = document.getElementById('dni').value;
        const calle = document.getElementById('calle').value;
        const numero = document.getElementById('numero').value;
        const localidad = document.getElementById('localidad').value;
        const provincia = document.getElementById('provincia').value;
        const fechaActual = new Date();
        fechaActual.setDate(fechaActual.getDate() + 1);
        const fechaIngreso = fechaActual.toISOString().split('T')[0];
        const newUser = {
            nombre,
            apellido,
            dni,
            fechaIngreso,
            domicilioEntradaDto: {
                calle,
                numero,
                localidad,
                provincia,
            }
        };
        hiddenElement.classList.add("form-hidden"); // Oculta el formulario
        try {
            const response = await fetch(`${apiUrl}/registrar`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(newUser)
            });
            if (!response.ok) {
                const errorData = await response.json();
                const errorsContainer = document.querySelector('.errors');
                errorsContainer.innerHTML = '';
                let claves = Object.keys(errorData);
                for (let i = 0; i < claves.length; i++) {
                    const pErrors = document.createElement('p');
                    let clave = claves[i];
                    pErrors.textContent = `* ${errorData[clave]} `;
                    errorsContainer.appendChild(pErrors);
                }
                hiddenElement.classList.remove("form-hidden");
                window.location.hash = 'form-hidden';
                return;
            }
            alert('Paciente registrado correctamente');
            hiddenElement.classList.add("form-hidden");
            fetchDataList();
        } catch (error) {
            console.error('Error:', error.message);
            alert(`Error: ${error.message}`);
        }
    };
    
    const updateUserData = async (event) => {
        event.preventDefault();
        const userId = document.getElementById('id').value;
        console.log("le id");
        console.log(userId);
        const nombre = document.getElementById('nombre').value;
        const apellido = document.getElementById('apellido').value;
        const dni = document.getElementById('dni').value;
        const calle = document.getElementById('calle').value;
        const numero = document.getElementById('numero').value;
        const localidad = document.getElementById('localidad').value;
        const provincia = document.getElementById('provincia').value;
        const fechaActual = new Date();
        fechaActual.setDate(fechaActual.getDate() + 1);
        const fechaIngreso = fechaActual.toISOString().split('T')[0];
        const updatedUser = {
            nombre,
            apellido,
            dni,
            fechaIngreso,
            domicilioEntradaDto: {
                calle,
                numero,
                localidad,
                provincia,
            }
        };
        console.log("Datos del usuario a actualizar:", updatedUser);
        try {
            const response = await fetch(`${apiUrl}/actualizar/${userId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(updatedUser)
            });
            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(`Error al actualizar los datos del usuario: ${errorData.message}`);
            }



            hiddenElement.classList.add("form-hidden");
            fetchDataList();
            messageUpdate.classList.remove("hidden");
            setTimeout(() => {
                messageUpdate.classList.add("hidden");
            }, 3000); 
        } catch (error) {
            console.error('Error:', error.message);
            alert(`Error: ${error.message}`);
        }
    };

    buttonBack.addEventListener('click', () => {
        buttonBack.classList.add("hidden");
        fetchDataList();
    });

    fetchDataList();
    addEventListeners();
});
