document.addEventListener('DOMContentLoaded', () => {
  const sidebar = document.querySelector('.sidebar');
  const toggle = document.querySelector('.sidebar-toggle');
  const root = document.documentElement;
  if (!sidebar || !toggle) {
    return;
  }

  const key = 'sidebarCollapsed';

  const apply = (collapsed) => {
    sidebar.classList.toggle('sidebar-collapsed', collapsed);
    root.classList.toggle('sidebar-collapsed', collapsed);
  };

  const stored = localStorage.getItem(key);
  if (stored === '1') {
    apply(true);
  }
  requestAnimationFrame(() => {
    root.classList.remove('sidebar-ready');
  });

  toggle.addEventListener('click', () => {
    root.classList.add('sidebar-animate');
    const collapsed = !sidebar.classList.contains('sidebar-collapsed');
    apply(collapsed);
    localStorage.setItem(key, collapsed ? '1' : '0');
  });
});
