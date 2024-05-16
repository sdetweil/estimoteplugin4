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

</docgen-api>
# estimoteplugin4
