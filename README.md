# estimoteplugin4

dfsdfdsf

## Install

```bash
npm install estimoteplugin4
npx cap sync
```

## API

<docgen-index>

* [`echo(...)`](#echo)
* [`createManager()`](#createmanager)
* [`startScanning(...)`](#startscanning)
* [`stopScanning(...)`](#stopscanning)
* [`connect(...)`](#connect)
* [`disconnect(...)`](#disconnect)
* [`checkPermissions()`](#checkpermissions)
* [`requestPermissions()`](#requestpermissions)
* [Interfaces](#interfaces)
* [Type Aliases](#type-aliases)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### echo(...)

```typescript
echo(options: { value: string; }) => Promise<{ handle: string; }>
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ value: string; }</code> |

**Returns:** <code>Promise&lt;{ handle: string; }&gt;</code>

--------------------


### createManager()

```typescript
createManager() => Promise<any>
```

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### startScanning(...)

```typescript
startScanning(handle: string, autoConnect: boolean, ids: string) => Promise<any>
```

| Param             | Type                 |
| ----------------- | -------------------- |
| **`handle`**      | <code>string</code>  |
| **`autoConnect`** | <code>boolean</code> |
| **`ids`**         | <code>string</code>  |

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### stopScanning(...)

```typescript
stopScanning(handle: string) => Promise<any>
```

| Param        | Type                |
| ------------ | ------------------- |
| **`handle`** | <code>string</code> |

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### connect(...)

```typescript
connect(handle: string, beacon: string) => Promise<any>
```

| Param        | Type                |
| ------------ | ------------------- |
| **`handle`** | <code>string</code> |
| **`beacon`** | <code>string</code> |

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### disconnect(...)

```typescript
disconnect(handle: string, beacon: string) => Promise<any>
```

| Param        | Type                |
| ------------ | ------------------- |
| **`handle`** | <code>string</code> |
| **`beacon`** | <code>string</code> |

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### checkPermissions()

```typescript
checkPermissions() => Promise<PermissionStatus>
```

**Returns:** <code>Promise&lt;<a href="#permissionstatus">PermissionStatus</a>&gt;</code>

--------------------


### requestPermissions()

```typescript
requestPermissions() => Promise<PermissionStatus>
```

**Returns:** <code>Promise&lt;<a href="#permissionstatus">PermissionStatus</a>&gt;</code>

--------------------


### Interfaces


#### PermissionStatus

| Prop          | Type                                                        |
| ------------- | ----------------------------------------------------------- |
| **`beacons`** | <code><a href="#permissionstate">PermissionState</a></code> |


### Type Aliases


#### PermissionState

<code>'prompt' | 'prompt-with-rationale' | 'granted' | 'denied'</code>

</docgen-api>
# estimoteplugin4
