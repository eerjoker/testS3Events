README

Parte 2:
Se configuraron las credenciales de AWS utilizando AWS CLI indicando access key y access secret key. Con esto se generó el .aws/credentials en el sistema operativo, fuera del proyecto para limitar el acceso a las credenciales de AWS.
Luego se agregaron las dependencias de AWS y SQS necesarias en el pom.xml. En el proyecto se crea el AwsConfiguration con el método sqsClient, el cual crea el cliente de SQS. Al ser marcado como inyectable dentro de un archivo de Configuration, el método queda accesaible dentro del proyecto para poder hacer peticiones a SQS y se construye una sola vez al iniciar la aplicación.
El Configuration obtiene los parametros necesarios para la conexión (region y si hay override del endpoint) de application.properties. Finalmente al hacer una petición llamando a sqsClient se indica el nombre de la queue a la que se le quiere enviar un mensaje.

Parte 3:
Una queue correctamente configurada no deberia tener mensajes duplicados gracias a que AWS ofrece realizar Content Based Deduplication en las queue FIFO. 
Asumiendo que el contenido del mensaje del evento de S3 es único entre el eventTime y el objectKey, SQS puede identificar los mensajes duplicados a partir de su contenido y preservar solo un mensaje.
Si pensamos en la posibilidad de que dos pods consuman el mismo mensaje en el mismo microsegundo, AWS implementa el Visibility Timeout como un sistema de lock. Con esta configuración SQS lockea el mensaje cuando se envía al primero de los dos solicitantes, evitando que el siguiente reciba también el mismo mensaje. Este timeout puede vencerse si el primer pod no llega a eliminar el mensaje al consumirlo por la longitud del mismo, se debe setear correctamente teniendo en cuenta las duraciones normales de los procesos.
