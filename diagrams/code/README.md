#Code used to generate Sequence Diagrams

Using:  https://bramp.github.io/js-sequence-diagrams/

##Basic Flow Diagrams
Title: Basic Flow
User/UI->>Gateway: HTTP Basic Authentication
Gateway-->>Auth Server: Pass along authentication request
Auth Server-->>Auth Server: Validate authentication
Auth Server-->>Auth Server: Generate ID Token
Auth Server-->>Auth Server: Generate Wallet Token
Auth Server->>Gateway: Return Wallet Token
Gateway->>User/UI: Store Wallet Token in Web Storage
User/UI->>Auth Server: Request Access Token
Auth Server-->>Auth Server:  Validate Wallet Token
Auth Server-->>Auth Server:  Validate ID Token
Auth Server->>User/UI: Return Access Token
User/UI->>Resource API: Request Access To Resource API
Note right of Resource API: Request to Access Resource API \ndoes not go through Auth Server
