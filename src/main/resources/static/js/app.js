let editando = false;

// Cargar todas las solicitudes
function cargarSolicitudes() {
    fetch('/solicitudes')
        .then(res => res.json())
        .then(data => {
            const tbody = document.querySelector('#tablaSolicitudes tbody');
            tbody.innerHTML = '';
            data.forEach(s => {
                const color = s.estado === 'Pendiente' ? 'table-warning' :
                              s.estado === 'En Proceso' ? 'table-info' :
                              s.estado === 'Completado' ? 'table-success' : '';
                const row = `<tr class="${color}">
                    <td>${s.id}</td>
                    <td>${s.descripcion}</td>
                    <td>${s.cliente?.nombre || ''}</td>
                    <td>${s.tecnicoAsignado?.nombre || ''}</td>
                    <td>${s.estado}</td>
                    <td>
                        <button class="btn btn-sm btn-warning" onclick="editarSolicitud(${s.id})">Editar</button>
                        <button class="btn btn-sm btn-danger" onclick="eliminarSolicitud(${s.id})">Eliminar</button>
                    </td>
                </tr>`;
                tbody.innerHTML += row;
            });
        });
}

// Crear o actualizar solicitud
document.querySelector('#formSolicitud').addEventListener('submit', function(e) {
    e.preventDefault();
    const id = document.querySelector('#solicitudId').value;
    const descripcion = document.querySelector('#descripcion').value;
    const clienteId = document.querySelector('#clienteId').value;
    const tecnicoId = document.querySelector('#tecnicoId').value;
    const estado = document.querySelector('#estado').value;

    const payload = {
        descripcion,
        cliente: { id: parseInt(clienteId) },
        tecnicoAsignado: tecnicoId ? { id: parseInt(tecnicoId) } : null,
        estado
    };

    const metodo = editando ? 'PUT' : 'POST';
    const url = editando ? `/solicitudes/${id}` : '/solicitudes';

    fetch(url, {
        method: metodo,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    })
    .then(res => {
        if (!res.ok) throw new Error('Error en la solicitud');
        return res.json();
    })
    .then(() => {
        editando = false;
        document.querySelector('#formSolicitud').reset();
        cargarSolicitudes();
    });
});

// Editar solicitud
function editarSolicitud(id) {
    fetch(`/solicitudes/${id}`)
        .then(res => res.json())
        .then(s => {
            document.querySelector('#solicitudId').value = s.id;
            document.querySelector('#descripcion').value = s.descripcion;
            document.querySelector('#clienteId').value = s.cliente?.id || '';
            document.querySelector('#tecnicoId').value = s.tecnicoAsignado?.id || '';
            document.querySelector('#estado').value = s.estado;
            editando = true;
        });
}

// Cancelar edición
document.querySelector('#cancelarEdicion').addEventListener('click', function() {
    editando = false;
    document.querySelector('#formSolicitud').reset();
});

// Eliminar solicitud
function eliminarSolicitud(id) {
    if (!confirm('¿Estás seguro de eliminar esta solicitud?')) return;
    fetch(`/solicitudes/${id}`, { method: 'DELETE' })
        .then(() => cargarSolicitudes());
}

// Inicializar
document.addEventListener('DOMContentLoaded', () => {
    cargarSolicitudes();
});
