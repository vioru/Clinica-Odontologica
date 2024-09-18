window.addEventListener("load", () => {
    const apiUrl = ['http://localhost:8080/turnos', 'shift'];
    const apiPatients = ['http://localhost:8080/pacientes', 'pacient'];
    const apiDentist = ['http://localhost:8080/odontologos', 'dentist'];
    const messageSection = document.getElementById('message-shift');
    const formCreated = document.getElementById('form-created');
    const listShift = document.getElementById('shift-list');
    const searchForm = document.getElementById('search-form');
    const buttonBack = document.getElementById('btn-back');

    console.log(formCreated, listShift, searchForm);

    const fetchDataList = async ([Url, type], id = null) => {


        try {
            const url = id ? `${Url}/listar?id=${id}` : `${Url}/listar`;
            const response = await fetch(url);
            if (!response.ok) {
                throw new Error('Error al obtener los datos');
            }
            const data = await response.json();

            const info = data.filter(item => item.odontologoSalidaDto || item.matricula || item.domicilioSalidaDto);

            if (info.length > 0) {
                const typeTable = `table-${type}`;
                const typeBody = `tbody-${type}`;

                renderTable(info, typeTable, typeBody);
            } else {
                console.log('No se encontraron datos relevantes.');
            }
        } catch (error) {
            console.error('Error:', error);
        }
    };

    const renderTable = (info, typeTable, typeBody) => {
        const table = document.querySelector(`.${typeTable}`);
        const tbody = table.querySelector(`.${typeBody}`);

        if (typeTable === "table-shift") {
            const thead = table.querySelector('thead');
            thead.innerHTML = '';

            const headerRow = document.createElement('tr');
            const headers = ["Turno N°", "Odontologo", "Paciente", "Eliminar"];
            headers.forEach(headerText => {
                const th = document.createElement('th');
                th.textContent = headerText;
                headerRow.appendChild(th);
            });
            thead.appendChild(headerRow);

            tbody.innerHTML = '';

            info.forEach(item => {
                const tr = document.createElement('tr');

                const tdId = document.createElement('td');
                tdId.textContent = item.id;
                tr.appendChild(tdId);

                const tdOdontologo = document.createElement('td');
                if (item.odontologoSalidaDto) {
                    const odontologo = `Dr ${item.odontologoSalidaDto.nombre} ${item.odontologoSalidaDto.apellido}`;
                    tdOdontologo.textContent = odontologo;
                }
                tr.appendChild(tdOdontologo);

                const tdPaciente = document.createElement('td');
                if (item.pacienteSalidaDto) {
                    const paciente = `${item.pacienteSalidaDto.nombre} ${item.pacienteSalidaDto.apellido}`;
                    tdPaciente.textContent = paciente;
                }
                tr.appendChild(tdPaciente);

                const tdDelete = document.createElement('td');
                if (item.id) {
                    const deleteButton = document.createElement('button');
                    deleteButton.textContent = 'Eliminar';
                    deleteButton.className = 'delete-btn'; // Clase para el botón
                    deleteButton.dataset.id = item.id; // Agrega el data-id con el ID del turno
                    tdDelete.appendChild(deleteButton);
                }
                tr.appendChild(tdDelete);

                tbody.appendChild(tr);
            });

            console.log(info);
            addEventListeners(); // Asegúrate de llamar a esta función después de renderizar la tabla

        } else {
            const claves = Object.keys(info[0]);

            tbody.innerHTML = '';

            info.forEach(item => {
                const tr = document.createElement('tr');

                const tdCheck = document.createElement('td');
                const check = document.createElement('input');
                check.type = 'checkbox';
                check.name = typeTable;
                check.value = item.id;

                check.addEventListener('change', (event) => {
                    const checkboxes = document.querySelectorAll(`input[name="${typeTable}"]`);
                    checkboxes.forEach(box => {
                        if (box !== event.target) {
                            box.checked = false;
                        }
                    });
                });

                tdCheck.appendChild(check);
                tr.appendChild(tdCheck);

                let nombreCompleto = "";

                claves.forEach(clave => {
                    if (clave === "nombre") {
                        nombreCompleto += item[clave];
                    }
                    if (clave === "apellido") {
                        nombreCompleto += " " + item[clave];
                    }
                });

                nombreCompleto = nombreCompleto.trim();

                const td = document.createElement('td');

                if (typeTable === "table-dentist") {
                    td.textContent = `Dr ${nombreCompleto}`;
                } else {
                    td.textContent = nombreCompleto;
                }

                tr.appendChild(td);
                tbody.appendChild(tr);
            });
        }
    };

    const addEventListeners = () => {
        const deleteButtons = document.querySelectorAll('.delete-btn');
        deleteButtons.forEach(button => {
            button.removeEventListener('click', deleteButtonClickHandler);  // Elimina listeners antiguos
            button.addEventListener('click', deleteButtonClickHandler);
        });
    };

    const deleteButtonClickHandler = async (e) => {
        const itemId = e.target.dataset.id;
        const confirmDelete = confirm('¿Estás seguro de que deseas borrar este turno?');
        if (confirmDelete) {
            await deleteUser(itemId);
        }
    };

    const deleteUser = async (id) => {
        try {
            const response = await fetch(`${apiUrl[0]}/eliminar?id=${id}`, {
                method: 'DELETE'
            });

            if (!response.ok) {
                alert('Error al borrar el turno');
                throw new Error('Error al borrar el turno');
            }

            alert('Turno eliminado con éxito');
            fetchDataList(apiUrl);
        } catch (error) {
            console.error('Error:', error);
        }
    };

    formCreated.addEventListener('submit', async function(event) {
        event.preventDefault();

        const selectedOdont = document.querySelector('input[name="table-dentist"]:checked');
        const selectedPacient = document.querySelector('input[name="table-pacient"]:checked');

        if (!selectedOdont || !selectedPacient) {
            alert("Para poder reservar un turno debe escoger un paciente y un odontólogo.");
        } else {
            const idOdontologo = selectedOdont.value;
            const idPaciente = selectedPacient.value;
            const fechaHora = new Date(new Date().setDate(new Date().getDate() + 1)).toISOString().slice(0, 19);

            console.log(fechaHora);

            const newShift = {
                idPaciente,
                idOdontologo,
                fechaHora
            };

            try {
                const response = await fetch(`${apiUrl[0]}/registrar`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(newShift)
                });

                if (response.ok) {
                    const result = await response.json();
                    fetchDataList(apiUrl);
                    // alert('Turno registrado con éxito');
                    formCreated.reset();
                    shiftMessageTable(result.id);
                    listShift.classList.remove("hidden");
                    messageSection.classList.remove("hidden");
                    formCreated.classList.add("hidden");
                    buttonBack.classList.remove("hidden");
                } else {
                    console.error('Error al registrar el turno:', response.statusText);
                    alert(`Error al registrar el turno: ${response.statusText || 'Error desconocido.'}`);
                }
            } catch (error) {
                console.error('Error al realizar la solicitud:', error);
            }
        }
    });

    function shiftMessageTable(shiftId) {
        messageSection.innerHTML = '';

        const table = document.createElement('table');
        table.className = 'message-table';

        const thead = document.createElement('thead');
        const headRow = document.createElement('tr');
        const emptyHeader = document.createElement('th');
        headRow.appendChild(emptyHeader);
        thead.appendChild(headRow);

        const tbody = document.createElement('tbody');
        const bodyRow = document.createElement('tr');
        const bodyCell = document.createElement('td');
        const message = document.createElement('h4');
        message.textContent = `Turno #${shiftId} creado con éxito`;
        message.className = 'shift-message';
        bodyCell.appendChild(message);
        bodyRow.appendChild(bodyCell);
        tbody.appendChild(bodyRow);

        table.appendChild(thead);
        table.appendChild(tbody);
        messageSection.appendChild(table);

        setTimeout(() => {
            messageSection.innerHTML = ''; 
            messageSection.classList.add("hidden");
        }, 2000); 
    }

    
    const btnBack = document.getElementById('btn-back');
    if (btnBack) {
        btnBack.addEventListener('click', () => {
            fetchDataList(apiPatients);
            fetchDataList(apiDentist);
            buttonBack.classList.add("hidden");
            listShift.classList.add("hidden");
            formCreated.classList.remove("hidden");
        });
    }


    fetchDataList(apiPatients);
    fetchDataList(apiDentist);
    fetchDataList(apiUrl);
});
