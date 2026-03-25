# 🚀 React Native + Expo + TypeScript Roadmap

Guia completo do **básico ao avançado**, com foco em prática e construção de um app real.

---

# 📦 Criação do Projeto (Template TypeScript)

```bash
npx create-expo-app meu-app --template blank-typescript
cd meu-app
npm install
npx expo start
```

Rodar web:

```bash
npx expo start --web
```

---

# 🧠 Semana 1 — Base TypeScript

## 🎯 Objetivo

Entender fundamentos que serão usados no React Native.

## 📚 Conteúdos

* function
* arrow function
* interface
* type
* enum

## 💻 Exemplo (App)

```tsx
import { View, Text } from 'react-native';

function saudacao(nome: string): string {
  return `Olá, ${nome}`;
}

const soma = (a: number, b: number): number => a + b;

type Status = 'loading' | 'success' | 'error';

export default function App() {
  const mensagem = saudacao('Anderson');
  const total = soma(10, 5);
  const status: Status = 'success';

  return (
    <View style={{ padding: 20 }}>
      <Text>{mensagem}</Text>
      <Text>Total: {total}</Text>
      <Text>Status: {status}</Text>
    </View>
  );
}
```

---

# 🧠 Semana 2 — Classes e Tipagem Avançada

## 📚 Conteúdos

* class
* extends
* implements
* readonly
* optional ( ? )

## 💻 Exemplo (App)

```tsx
import { View, Text } from 'react-native';

interface Animal {
  name: string;
  makeSound(): string;
}

class Dog implements Animal {
  constructor(public name: string) {}

  makeSound(): string {
    return 'Au au';
  }
}

class Vehicle {
  constructor(public brand: string) {}
}

class Car extends Vehicle {
  constructor(brand: string, public model: string) {
    super(brand);
  }
}

export default function App() {
  const dog = new Dog('Rex');
  const car = new Car('Toyota', 'Corolla');

  return (
    <View style={{ padding: 20 }}>
      <Text>{dog.name} faz {dog.makeSound()}</Text>
      <Text>{car.brand} - {car.model}</Text>
    </View>
  );
}
```

---

# 📱 Semana 3 — Fundamentos React Native

## 📚 Conteúdos

* JSX
* View, Text
* TextInput, Button
* useState

## 💻 Exemplo (App)

```tsx
import { View, Text, TextInput, Button } from 'react-native';
import { useState } from 'react';

export default function App() {
  const [name, setName] = useState<string>('');

  return (
    <View style={{ padding: 20 }}>
      <Text>Digite seu nome:</Text>
      <TextInput value={name} onChangeText={setName} style={{ borderWidth: 1 }} />
      <Button title="Mostrar" onPress={() => alert(name)} />
    </View>
  );
}
```

---

# 🎨 Semana 4 — Layout + Estilo

## 📦 Instalação

```bash
npx expo install react-native-safe-area-context
```

## 📚 Conteúdos

* Flexbox
* StyleSheet

## 💻 Exemplo (App)

```tsx
import { View, Text, StyleSheet } from 'react-native';

export default function App() {
  return (
    <View style={styles.container}>
      <View style={styles.box}><Text>1</Text></View>
      <View style={styles.box}><Text>2</Text></View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, flexDirection: 'row', justifyContent: 'space-around', alignItems: 'center' },
  box: { width: 80, height: 80, backgroundColor: 'lightblue', justifyContent: 'center', alignItems: 'center' }
});
```

---

# 🔄 Semana 5 — Estado + Eventos

## 📚 Conteúdos

* useState
* Eventos (onPress)

## 💻 Exemplo (App)

```tsx
import { View, Text, Button } from 'react-native';
import { useState } from 'react';

export default function App() {
  const [count, setCount] = useState<number>(0);

  return (
    <View style={{ padding: 20 }}>
      <Text>Contador: {count}</Text>
      <Button title="+" onPress={() => setCount(count + 1)} />
    </View>
  );
}
```

---

# 🧭 Semana 6 — Navegação

## 📦 Instalação

```bash
npm install @react-navigation/native
npm install @react-navigation/native-stack
npx expo install react-native-screens react-native-safe-area-context
```

## 💻 Exemplo (App)

```tsx
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { View, Text, Button } from 'react-native';

const Stack = createNativeStackNavigator();

function Home({ navigation }: any) {
  return (
    <View>
      <Text>Home</Text>
      <Button title="Ir" onPress={() => navigation.navigate('Details')} />
    </View>
  );
}

function Details() {
  return <Text>Detalhes</Text>;
}

export default function App() {
  return (
    <NavigationContainer>
      <Stack.Navigator>
        <Stack.Screen name="Home" component={Home} />
        <Stack.Screen name="Details" component={Details} />
      </Stack.Navigator>
    </NavigationContainer>
  );
}
```

---

# 🌐 Semana 7 — API + Listas

## 📦 Instalação

```bash
npm install axios
```

## 💻 Exemplo (App)

```tsx
import { useEffect, useState } from 'react';
import { FlatList, Text } from 'react-native';

interface User {
  id: number;
  name: string;
}

export default function App() {
  const [users, setUsers] = useState<User[]>([]);

  useEffect(() => {
    fetch('https://jsonplaceholder.typicode.com/users')
      .then(res => res.json())
      .then(setUsers);
  }, []);

  return (
    <FlatList
      data={users}
      keyExtractor={(item) => item.id.toString()}
      renderItem={({ item }) => <Text>{item.name}</Text>}
    />
  );
}
```

---

# 💾 Semana 8 — Persistência

## 📦 Instalação

```bash
npx expo install @react-native-async-storage/async-storage
```

## 💻 Exemplo (App)

```tsx
import { View, Button, Text } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useState } from 'react';

export default function App() {
  const [token, setToken] = useState<string>('');

  const save = async () => {
    await AsyncStorage.setItem('token', '123');
    setToken('123');
  };

  return (
    <View style={{ padding: 20 }}>
      <Text>Token: {token}</Text>
      <Button title="Salvar" onPress={save} />
    </View>
  );
}
```

---

# 🌍 Semana 9 — Context API

## 💻 Exemplo (App)

```tsx
import { createContext, useContext, useState } from 'react';
import { View, Text, Button } from 'react-native';

const AuthContext = createContext<any>(null);

function Home() {
  const { user, setUser } = useContext(AuthContext);

  return (
    <View>
      <Text>{user}</Text>
      <Button title="Login" onPress={() => setUser('Anderson')} />
    </View>
  );
}

export default function App() {
  const [user, setUser] = useState<string | null>(null);

  return (
    <AuthContext.Provider value={{ user, setUser }}>
      <Home />
    </AuthContext.Provider>
  );
}
```

---

# 📸 Semana 10 — Recursos Nativos

## 📦 Instalação

```bash
npx expo install expo-camera
```

## 💻 Exemplo (App)

```tsx
import { CameraView, useCameraPermissions } from 'expo-camera';
import { View, Button } from 'react-native';

export default function App() {
  const [permission, requestPermission] = useCameraPermissions();

  if (!permission?.granted) {
    return (
      <View>
        <Button title="Permitir" onPress={requestPermission} />
      </View>
    );
  }

  return <CameraView style={{ flex: 1 }} />;
}
```

---

# 💣 Projeto Final

## 🎯 Objetivo

Criar app completo integrando tudo.

## Funcionalidades

* Login
* Lista
* Navegação
* API
* Context

---

# 🧱 Estrutura sugerida

```
src/
 ├── screens/
 ├── components/
 ├── services/
 ├── hooks/
 ├── context/
 └── utils/
```

---

# 🚀 Próximo nível

* Keycloak
* Spring Boot
* Arquitetura enterprise

---

# 🧠 Conclusão

Você estará apto a criar apps profissionais.

🔥 Ready!
