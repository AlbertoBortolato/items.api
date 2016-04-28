
# Items Api README


Aggiungete la diretiva per il `reverse-proxy`:

``` nginx
        location /api/ {
            proxyPass   http://localhost:8080/api/ ;
        }
```