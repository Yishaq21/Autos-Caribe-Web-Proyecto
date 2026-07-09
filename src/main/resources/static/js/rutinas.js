
//Para insertar información en el modal según el registro...
document.addEventListener("DOMContentLoaded", function () {

    const confirmModal = document.getElementById("confirmModal");

    if (confirmModal) {

        confirmModal.addEventListener("show.bs.modal", function (event) {

            const button = event.relatedTarget;

            const id = button.getAttribute("data-bs-id");
            const descripcion = button.getAttribute("data-bs-descripcion");

            document.getElementById("modalId").value = id;
            document.getElementById("modalDescripcion").textContent = descripcion;

        });

    }

});