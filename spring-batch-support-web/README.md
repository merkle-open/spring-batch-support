# Spring Batch Support Web

Use to start/stop batch jobs over web servlet. 

### include as iframe
The js app will automatically setup CORS-Header for ajax requests, if required parameters are provided on integration.

If you use iframe integration, provide the following params with your src url.

```html
<iframe src="/batch/detail.html#csrf=${_csrf.token}&csrf_header=${_csrf.headerName}" width="100%"></iframe>
```

Param        | Value
-------------|--------------------------------------------------
csrf         | actual token to be send with every request 
csrf_header  | HTTP-Header name to send token with every request

### overview
There is an overview, with all existing jobs for this jobOperator. Your could start/stop a job directly, got to the job detail page or check exceptions on failed jobs.
Example View: 
![Example View](readme/example-overview.png)
 

