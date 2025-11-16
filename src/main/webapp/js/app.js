/**
 * GestionCoiffure - Application JavaScript Utilities
 * Shared functions and utilities for the entire application
 */

// Password visibility toggle utility
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

// Form validation utility
function validateForm(formId, rules) {
    const form = document.getElementById(formId);
    if (!form) return;

    form.addEventListener('submit', function(e) {
        let isValid = true;
        const errors = [];

        // Run custom validation rules
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

// Toast notification system
const Toast = {
    show: function(message, type = 'info', duration = 3000) {
        const toast = document.createElement('div');
        toast.className = `toast toast-${type}`;
        toast.innerHTML = `
            <i class="fas fa-${this.getIcon(type)}"></i>
            <span>${message}</span>
        `;

        document.body.appendChild(toast);

        // Trigger animation
        setTimeout(() => toast.classList.add('show'), 10);

        // Auto remove
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

// Confirmation dialog utility
function confirmAction(message, callback) {
    if (confirm(message)) {
        callback();
    }
}

// Debounce utility for search inputs
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

// Format date utility
function formatDate(dateString) {
    const date = new Date(dateString);
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return date.toLocaleDateString('fr-FR', options);
}

// Format time utility
function formatTime(timeString) {
    return timeString.substring(0, 5); // HH:MM format
}

// Initialize all utilities when DOM is ready
document.addEventListener('DOMContentLoaded', function() {
    initPasswordToggles();

    // Auto-hide alerts after 5 seconds
    document.querySelectorAll('.alert').forEach(alert => {
        setTimeout(() => {
            alert.style.opacity = '0';
            alert.style.transition = 'opacity 0.3s';
            setTimeout(() => alert.remove(), 300);
        }, 5000);
    });

    // Add confirmation to delete buttons
    document.querySelectorAll('[data-confirm]').forEach(element => {
        element.addEventListener('click', function(e) {
            const message = this.getAttribute('data-confirm');
            if (!confirm(message)) {
                e.preventDefault();
                return false;
            }
        });
    });

    // Set minimum date for date inputs to today
    document.querySelectorAll('input[type="date"]').forEach(input => {
        if (!input.hasAttribute('min')) {
            input.setAttribute('min', new Date().toISOString().split('T')[0]);
        }
    });
});

// Export utilities for use in other scripts
window.GestionCoiffure = {
    Toast,
    confirmAction,
    debounce,
    formatDate,
    formatTime,
    validateForm
};
