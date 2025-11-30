 

function initPasswordToggles() {
    document.querySelectorAll('[data-password-toggle]').forEach(button => {
        button.addEventListener('click', function() {
            const targetId = this.getAttribute('data-password-toggle');
            const input = document.getElementById(targetId);
            if (!input) return;

            const type = input.getAttribute('type') === 'password' ? 'text' : 'password';
            input.setAttribute('type', type);

            const icon = this.querySelector('i');
            if (icon) {
                icon.classList.toggle('fa-eye');
                icon.classList.toggle('fa-eye-slash');
            }
        });
    });
}

function validateForm(formId, rules) {
    const form = document.getElementById(formId);
    if (!form) return;

    form.addEventListener('submit', function(e) {
        let isValid = true;
        const errors = [];

        if (rules) {
            for (const [field, validator] of Object.entries(rules)) {
                const input = form.querySelector(`[name="${field}"]`);
                if (input && !validator(input.value)) {
                    isValid = false;
                    errors.push(field);
                }
            }
        }

        if (!isValid) {
            e.preventDefault();
            console.error('Form validation failed:', errors);
        }
    });
}

const Toast = {
    show: function(message, type = 'info', duration = 3000) {
        const toast = document.createElement('div');
        toast.className = `toast toast-${type}`;
        toast.innerHTML = `
            <i class="fas fa-${this.getIcon(type)}"></i>
            <span>${message}</span>
        `;

        document.body.appendChild(toast);

        setTimeout(() => toast.classList.add('show'), 10);

        setTimeout(() => {
            toast.classList.remove('show');
            setTimeout(() => toast.remove(), 300);
        }, duration);
    },

    getIcon: function(type) {
        const icons = {
            success: 'check-circle',
            error: 'exclamation-circle',
            warning: 'exclamation-triangle',
            info: 'info-circle'
        };
        return icons[type] || icons.info;
    }
};

function confirmAction(message, callback) {
    if (confirm(message)) {
        callback();
    }
}

function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

function formatDate(dateString) {
    const date = new Date(dateString);
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return date.toLocaleDateString('fr-FR', options);
}

function formatTime(timeString) {
    return timeString.substring(0, 5);  
}

document.addEventListener('DOMContentLoaded', function() {
    initPasswordToggles();

    document.querySelectorAll('.alert').forEach(alert => {
        setTimeout(() => {
            alert.style.opacity = '0';
            alert.style.transition = 'opacity 0.3s';
            setTimeout(() => alert.remove(), 300);
        }, 5000);
    });

    document.querySelectorAll('[data-confirm]').forEach(element => {
        element.addEventListener('click', function(e) {
            const message = this.getAttribute('data-confirm');
            if (!confirm(message)) {
                e.preventDefault();
                return false;
            }
        });
    });

    document.querySelectorAll('input[type="date"]').forEach(input => {
        if (!input.hasAttribute('min')) {
            input.setAttribute('min', new Date().toISOString().split('T')[0]);
        }
    });
});

window.GestionCoiffure = {
    Toast,
    confirmAction,
    debounce,
    formatDate,
    formatTime,
    validateForm
};
